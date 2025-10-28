import { tokenService } from "../services/tokenService";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

type RequestOptions = RequestInit & { auth?: boolean };

async function getValidAccessToken(): Promise<string | null> {
  const token = tokenService.get();
  if (!token) return null;

  const payload = JSON.parse(atob(token.split(".")[1]));
  const now = Math.floor(Date.now() / 1000);
  if (payload.exp > now) return token;

  try {
    const res = await fetch(`${BASE_URL}/auth/refresh`, {
      method: "POST",
      credentials: "include",
    });
    if (!res.ok) throw new Error("No valid refresh token");

    const data = await res.json();
    tokenService.set(data.accessToken);
    localStorage.setItem("accessToken", data.accessToken);
    return data.accessToken;
  } catch {
    tokenService.remove();
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    return null;
  }
}

export async function fetchClient<T>(endpoint: string, options: RequestOptions = {}) {
  const headers: Record<string, string> = { ...(options.headers as Record<string, string>) };

  if (!(options.body instanceof FormData)) headers["Content-Type"] = "application/json";

  if (options.auth) {
    const token = await getValidAccessToken();
    if (!token) throw new Error("Not authenticated");
    headers.Authorization = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });

  if (!res.ok) {
    const message = await res.text();
    throw new Error(message || `Request failed: ${res.status}`);
  }

  if (res.status === 204) return undefined as T;

  return res.json() as Promise<T>;
}