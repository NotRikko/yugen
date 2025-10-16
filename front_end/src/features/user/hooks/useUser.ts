import { useState, useEffect } from "react";
import { User, Cart } from "../types/userTypes";
import { userApi } from "../api/userApi";

const guestUser: User = {
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
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = async (username: string, password: string) => {
    const API_URL = import.meta.env.VITE_API_URL;

    const res = await fetch(`${API_URL}/users/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const data = await res.json();
    if (!res.ok) throw new Error(data.message || "Login failed");

    if (data.token) localStorage.setItem("accessToken", data.token);

    const token = localStorage.getItem("accessToken");
    const userRes = await fetch(`${API_URL}/users/me`, {
      method: "GET",
      headers: { Authorization: `Bearer ${token}` },
    });

    const userData = await userRes.json();
    if (!userRes.ok) throw new Error(userData.message || "Failed to fetch user");

    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    setIsLoggedIn(true);

  };

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    setUser(guestUser);
    setCart(null);
    setIsLoggedIn(false);
  };

  useEffect(() => {
    const cachedUser = localStorage.getItem("user");

    if (cachedUser) {
      setUser(JSON.parse(cachedUser));
      setIsLoggedIn(true);
    }
    
    const token = localStorage.getItem("accessToken");
    if (!token) return;
  
    const fetchUser = async () => {
      try {
        const data = await userApi.getCurrentUser();
        if (data) setUser(data);
        setIsLoggedIn(true);
        const cartData = await userApi.getCart();
        setCart(cartData);
      } catch (err) {
        console.error(err);
        handleLogout(); 
      }
    };
  
    fetchUser();
  }, []);

    return { user, setUser, cart, setCart, isLoggedIn, handleLogin, handleLogout };
};