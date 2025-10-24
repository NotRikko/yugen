import { useCallback } from "react";
import { followApi } from "../api/followApi";

export const useFollow = () => {
  const followArtist = useCallback(
    (artistId: number) => followApi.followArtist(artistId),
    []
  );

  const unfollowArtist = useCallback(
    (artistId: number) => followApi.unfollowArtist(artistId),
    []
  );

  const getFollowers = useCallback(
    async (artistId: number) => {
      return await followApi.getFollowers(artistId);
    },
    []
  );

  const getFollowing = useCallback(
    (userId: number) => followApi.getFollowing(userId),
    []
  );

  const checkIfFollowing = useCallback(
    (artistId: number) => followApi.checkIfFollowing(artistId),
    []
  );

  return { followArtist, unfollowArtist, getFollowers, getFollowing, checkIfFollowing };
};