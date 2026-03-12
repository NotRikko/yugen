import { useState } from "react";
import type { UserDTO } from "../types";
import type { CartDTO } from "@/features/cart/types";
import { tokenService } from "@/shared/services/tokenService";

interface FrontendUserDTO extends UserDTO {
  isGuest?: boolean;
}
export const guestUser: FrontendUserDTO = {
  id: 0,
  username: "Guest",
  displayName: "Guest User",
  email: "",
  image: "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg",
  artistId: undefined,
  isGuest: true,
};

export const useUserHook = () => {
  const [user, setUser] = useState<FrontendUserDTO>(() => {
    const cached = localStorage.getItem("user");
    return cached ? JSON.parse(cached) : guestUser;
  });

  const [cart, setCart] = useState<CartDTO | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState(() => !!localStorage.getItem("accessToken"));

  const API_URL = import.meta.env.VITE_API_URL;

  const handleLogin = async (username: string, password: string) => {
    const res = await fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
      credentials: "include",
    });

    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(data?.error || "Invalid username or password");

    tokenService.set(data.accessToken);
    localStorage.setItem("accessToken", data.accessToken);

    const userRes = await fetch(`${API_URL}/users/me`, {
      headers: { Authorization: `Bearer ${data.accessToken}` },
    });

    const userData = await userRes.json().catch(() => ({}));
    if (!userRes.ok) throw new Error(userData?.error || "Failed to fetch user");

    setUser(userData);
    setIsLoggedIn(true);
    localStorage.setItem("user", JSON.stringify(userData));
  };

  const handleLogout = () => {
    tokenService.remove();
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");

    setUser(guestUser);
    setCart(null);
    setIsLoggedIn(false);

    fetch(`${API_URL}/auth/logout`, { method: "POST", credentials: "include" }).catch(() => {});
  };

  return { user, setUser, cart, setCart, isLoggedIn, handleLogin, handleLogout };
};