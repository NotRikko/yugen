import { fetchClient } from "@/shared/api/fetchClient";
import type { Follow } from "../types/followTypes";
import type { PartialUser } from "@/features/user/types/userTypes";
export const followApi = {
  followArtist: async (artistId: number) => {
    return fetchClient<Follow>(`/follow/artist/${artistId}`, {
      method: "POST",
      auth: true,
    });
  },

  unfollowArtist: async (artistId: number) => {
    return fetchClient<void>(`/follow/artist/${artistId}`, {
      method: "DELETE",
      auth: true,
    });
  },

  checkIfFollowing: (artistId: number) => {
    return fetchClient<boolean>(`/follow/artist/${artistId}/check`, {
      method: "GET",
      auth: true,
    });
  },

  getFollowers: async (artistId: number) => {
    return fetchClient<PartialUser[]>(`/follow/artist/${artistId}/followers`, {
      method: "GET",
      auth: true,
    });
  },

  getFollowing: (userId: number) => {
    return fetchClient<PartialUser[]>(`/follow/user/${userId}/following`, {
      method: "GET",
      auth: true,
    });
  },
};