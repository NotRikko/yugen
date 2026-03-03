import { fetchClient } from "@/shared/api/fetchClient";
import type { ArtistSummaryDTO } from "@/features/artists/types";
import type { UserDTO } from "@/features/user/types";
import type { FollowWithUserDTO } from "../types";

export const followApi = {
  followArtist: async (artistId: number): Promise<FollowWithUserDTO> => {
    return fetchClient<FollowWithUserDTO>(`/artists/${artistId}/followers`, {
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
    return fetchClient<UserDTO[]>(`/artists/${artistId}/followers`, {
      method: "GET",
    });
  },

  getFollowing: async(userId: number) => {
    return fetchClient<ArtistSummaryDTO[]>(`/users/${userId}/following`, {
      method: "GET",
    })
  },

  getFollowingForCurrentUser: () => {
    return fetchClient<ArtistSummaryDTO[]>(`/users/me/following`, {
      method: "GET",
      auth: true,
    });
  },
};