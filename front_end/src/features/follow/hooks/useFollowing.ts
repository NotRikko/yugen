import { useState, useEffect, useCallback } from "react";
import type { ArtistSummaryDTO } from "@/features/artists/types";
import { followApi } from "../api/followApi";

export const useFollowing = (userId?: number) => {
  const [following, setFollowing] = useState<ArtistSummaryDTO[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchFollowing = useCallback(async () => {
    setLoading(true);
    try {
      let res: ArtistSummaryDTO[] = [];
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