import { fetchClient } from "@/shared/api/fetchClient";
import { PartialArtist, Artist } from "../types/artistTypes";
export const artistApi = {
  getArtists: () =>
    fetchClient<Artist[]>("/artists/", { method: "GET"}),

  getById: (artistId: number) =>
    fetchClient<PartialArtist>(`/artists/${artistId}`, { method: "GET" }),

  getByArtistName: (artistName: string) =>
    fetchClient<PartialArtist>(`/artists/name/${artistName}`, { method: "GET" }),

  updateArtist: (artistId: number, updates: Partial<Artist>) =>
    fetchClient<Artist>(`/artists/${artistId}`, {
      method: "PATCH",
      body: JSON.stringify(updates),
      auth: true,
    }),
}