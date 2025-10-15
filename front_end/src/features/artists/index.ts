import { Artist, Post } from "../types/artistTypes";
import { Product } from "../types/productTypes";

const BASE_URL = import.meta.env.VITE_API_URL; 

// helper to handle fetch responses
async function fetchJson<T>(url: string): Promise<T> {
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`HTTP error! status: ${res.status}`);
  }
  return res.json() as Promise<T>;
}

export async function fetchArtistById(artistId: number): Promise<Artist> {
  return fetchJson<Artist>(`${BASE_URL}/artists/${artistId}`);
}

export async function fetchArtistPosts(artistId: number): Promise<Post[]> {
  return fetchJson<Post[]>(`${BASE_URL}/artists/${artistId}/posts`);
}

export async function fetchArtistProducts(artistId: number): Promise<Product[]> {
  return fetchJson<Product[]>(`${BASE_URL}/artists/${artistId}/products`);
}

export async function fetchArtistByName(name: string): Promise<Artist[]> {
  const query = encodeURIComponent(name);
  return fetchJson<Artist[]>(`${BASE_URL}/artists?name=${query}`);
}