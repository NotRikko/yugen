import { fetchClient } from "@/shared/api/fetchClient";
import { PartialArtist, Artist } from "../types/artistTypes";
import { Post } from ".."
import { Product } from "../types/productTypes";

export const artistApi = {
  getArtists: () =>
    fetchClient<Artist[]>("/artists/", { method: "GET"}),

  getById: (artistId: number) =>
    fetchClient<PartialArtist>(`/artists/${artistId}`, { method: "GET" }),

  getPosts: (artistId: number) =>
    fetchClient<Post[]>(`/artists/${artistId}/posts`, { method: "GET" }),

  getProducts: (artistId: number) =>
    fetchClient<Product[]>(`/artists/${artistId}/products`, { method: "GET" }),

  createPost: (artistId: number, data: { title: string; content: string }) =>
    fetchClient<Post>(`/artists/${artistId}/posts`, {
      method: "POST",
      body: JSON.stringify(data),
      auth: true, 
    }),

  updateArtist: (artistId: number, updates: Partial<Artist>) =>
    fetchClient<Artist>(`/artists/${artistId}`, {
      method: "PATCH",
      body: JSON.stringify(updates),
      auth: true,
    }),

  deleteProduct: (artistId: number, productId: number) =>
    fetchClient<void>(`/artists/${artistId}/products/${productId}`, {
      method: "DELETE",
      auth: true,
    }),
}