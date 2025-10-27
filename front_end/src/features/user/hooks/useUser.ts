import { useEffect, useState } from "react";
import type { User } from "../types/userTypes";
import type { Cart } from "@/features/cart/types/cartTypes";
import { tokenService } from "@/shared/services/tokenService";

export const guestUser: User = {
  id: 0,
  username: "Guest",
  displayName: "Guest User",
  email: "",
  image: "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg",
  artistId: null,
  isGuest: true,
};

function isTokenExpired(token: string | null): boolean {
  if (!token) return true;
  try {
    const [, payloadBase64] = token.split(".");
    const payload = JSON.parse(atob(payloadBase64));
    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
  } catch {
    return true;
  }
}

export const useUserHook = () => {
  const [user, setUser] = useState<User>(() => {
    const cached = localStorage.getItem("user");
    return cached ? JSON.parse(cached) : guestUser;
  });

  const [cart, setCart] = useState<Cart | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState(() => !!localStorage.getItem("accessToken"));

  const handleLogin = async (username: string, password: string) => {
    const API_URL = import.meta.env.VITE_API_URL;

    const res = await fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
      credentials: "include",
    });

    const data = await res.json();
    if (!res.ok) throw new Error(data?.message || "Invalid username or password");

    tokenService.set(data.accessToken);
    localStorage.setItem("accessToken", data.accessToken);

    const userRes = await fetch(`${API_URL}/users/me`, {
      headers: { Authorization: `Bearer ${data.accessToken}` },
    });

    const userData = await userRes.json();
    if (!userRes.ok) throw new Error(userData.message || "Failed to fetch user");

    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    tokenService.remove();
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    setUser(guestUser);
    setCart(null);
    setIsLoggedIn(false);
  };

  useEffect(() => {
    const API_URL = import.meta.env.VITE_API_URL;

    const refreshAccessToken = async () => {
      try {
        const res = await fetch(`${API_URL}/auth/refresh`, {
          method: "POST",
          credentials: "include",
        });

        if (!res.ok) throw new Error("No valid refresh token");

        const data = await res.json();
        tokenService.set(data.accessToken);
        localStorage.setItem("accessToken", data.accessToken);
        setIsLoggedIn(true);

        if (!user || user.isGuest) {
          const userRes = await fetch(`${API_URL}/users/me`, {
            headers: { Authorization: `Bearer ${data.accessToken}` },
          });
          if (userRes.ok) {
            const userData = await userRes.json();
            setUser(userData);
            localStorage.setItem("user", JSON.stringify(userData));
          }
        }
      } catch {
        console.debug("No valid refresh token; switching to guest mode");
        handleLogout();
      }
    };

  const checkToken = async () => {
    const token = tokenService.get() || localStorage.getItem("accessToken");
    if (!token || isTokenExpired(token)) {
      await refreshAccessToken();
    } else {
      setIsLoggedIn(true);
    }
  };

  checkToken();

  const interval = setInterval(() => {
    const token = tokenService.get() || localStorage.getItem("accessToken");
    if (isTokenExpired(token)) {
      refreshAccessToken();
    }
  }, 15 * 60 * 1000); 

  return () => clearInterval(interval);
}, [user]);; 

  return { user, setUser, cart, setCart, isLoggedIn, handleLogin, handleLogout };
};