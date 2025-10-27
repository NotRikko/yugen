import { fetchClient } from "@/shared/api/fetchClient";
import type { Follow } from "../types/followTypes";
import type { PartialUser } from "@/features/user/types/userTypes";
import type { PartialArtist } from "@/features/artists/types/artistTypes";
export const followApi = {
  followArtist: async (artistId: number) => {
    return fetchClient<Follow>(`/artists/${artistId}/followers`, {
      method: "POST",
      auth: true,
    });
  },

  unfollowArtist: async (artistId: number) => {
    return fetchClient<void>(`/artists/${artistId}/followers`, {
      method: "DELETE",
      auth: true,
    });
  },

  checkIfFollowing: (artistId: number) => {
    return fetchClient<boolean>(`/artists/${artistId}/check`, {
      method: "GET",
      auth: true,
    });
  },

  getFollowers: async (artistId: number) => {
    return fetchClient<PartialUser[]>(`/artists/${artistId}/followers`, {
      method: "GET",
    });
  },

  getFollowing: async(userId: number) => {
    return fetchClient<PartialArtist[]>(`/users/${userId}/following`, {
      method: "GET",
    })
  },

  getFollowingForCurrentUser: () => {
    return fetchClient<PartialArtist[]>(`/users/me/following`, {
      method: "GET",
      auth: true,
    });
  },
};