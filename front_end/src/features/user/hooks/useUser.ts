import { useState } from "react";
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

    fetch(`${import.meta.env.VITE_API_URL}/auth/logout`, {
      method: "POST",
      credentials: "include",
    }).catch(() => {});
  };

  return { user, setUser, cart, setCart, isLoggedIn, handleLogin, handleLogout };
};