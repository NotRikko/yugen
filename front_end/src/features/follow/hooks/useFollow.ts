import { useState, useEffect, useCallback } from "react";
import { followApi } from "../api/followApi";

export const useFollow = (artistId: number) => {
  const [isFollowing, setIsFollowing] = useState<boolean | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadStatus = async () => {
      try {
        const status = await followApi.checkIfFollowing(artistId);
        setIsFollowing(status);
      } catch (e) {
        console.error("Failed to check follow status", e);
      }
    };
    loadStatus();
  }, [artistId]);

   const toggleFollow = useCallback(async () => {
    if (isFollowing === null) return;
    setLoading(true);

    try {
      if (isFollowing) {
        await followApi.unfollowArtist(artistId);
      } else {
        await followApi.followArtist(artistId);
      }
      setIsFollowing(!isFollowing);
    } catch (e) {
      console.error("Failed to toggle follow:", e);
    } finally {
      setLoading(false);
    }
  }, [artistId, isFollowing]);

  return { isFollowing, loading, toggleFollow };
};