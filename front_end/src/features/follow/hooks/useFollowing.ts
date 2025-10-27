import { useState, useEffect, useCallback } from "react";
import type { PartialArtist } from "@/features/artists/types/artistTypes";
import { followApi } from "../api/followApi";

export const useFollowing = (userId?: number) => {
  const [following, setFollowing] = useState<PartialArtist[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchFollowing = useCallback(async () => {
    setLoading(true);
    try {
      let res: PartialArtist[] = [];
      if (userId) {
        res = await followApi.getFollowing(userId);
      } else {
        res = await followApi.getFollowingForCurrentUser();
      }
      setFollowing(res || []);
    } catch (err) {
      console.error("Failed to fetch following:", err);
      setFollowing([]);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchFollowing();
  }, [fetchFollowing]);

  return { following, loading, refetch: fetchFollowing };
};