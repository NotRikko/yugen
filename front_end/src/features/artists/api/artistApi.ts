import { fetchClient } from "@/shared/api/fetchClient";
import type { ArtistDTO } from "../types";
export const artistApi = {
  getArtists: () =>
    fetchClient<ArtistDTO[]>("/artists", { method: "GET"}),

  getById: (artistId: number) =>
    fetchClient<ArtistDTO>(`/artists/${artistId}`, { method: "GET" }),

  getByArtistName: (artistName: string) =>
    fetchClient<ArtistDTO>(`/artists/name/${artistName}`, { method: "GET" }),

  updateArtist: (artistId: number, updates: Partial<ArtistDTO>) =>
    fetchClient<ArtistDTO>(`/artists/${artistId}`, {
      method: "PATCH",
      body: JSON.stringify(updates),
      auth: true,
    }),
}