import { tokenService } from "../services/tokenService";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

type RequestOptions = RequestInit & { auth?: boolean };

export async function fetchClient<T>(endpoint: string, options: RequestOptions = {}) {
  const headers: Record<string, string> = { ...(options.headers as Record<string, string>) };

  if (!(options.body instanceof FormData)) headers["Content-Type"] = "application/json";

  if (options.auth) {
    const token = tokenService.get();
    if (token) headers.Authorization = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });

  if (!res.ok) {
    const message = await res.text();
    throw new Error(message || `Request failed: ${res.status}`);
  }

  if (res.status === 204) return undefined as T;

  return res.json() as Promise<T>;
}