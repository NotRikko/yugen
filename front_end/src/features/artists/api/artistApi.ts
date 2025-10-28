import { fetchClient } from "@/shared/api/fetchClient";
import { PartialArtist, Artist } from "../types/artistTypes";
import { PostDTO } from "@/features/posts/types/postTypes";
import { Product } from "@/features/user/types/userTypes";

export const artistApi = {
  getArtists: () =>
    fetchClient<Artist[]>("/artists/", { method: "GET"}),

  getById: (artistId: number) =>
    fetchClient<PartialArtist>(`/artists/${artistId}`, { method: "GET" }),

  getByArtistName: (artistName: string) =>
    fetchClient<PartialArtist>(`/artists/name/${artistName}`, { method: "GET" }),

  getPostsById: (artistId: number) =>
    fetchClient<PostDTO[]>(`/artists/${artistId}/posts`, { method: "GET" }),

  getPostsByArtistName: (artistName: string) =>
    fetchClient<PostDTO[]>(`/artists/${artistName}/posts`, { method: "GET" }),
  
  getProducts: (artistId: number) =>
    fetchClient<Product[]>(`/artists/${artistId}/products`, { method: "GET" }),

  createPost: (artistId: number, data: { title: string; content: string }) =>
    fetchClient<PostDTO>(`/artists/${artistId}/posts`, {
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