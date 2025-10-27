import { useState, useEffect } from "react";
import type { PartialUser } from "@/features/user/types/userTypes";
import { followApi } from "../api/followApi";

export const useFollowers = (artistId?: number) => {
  const [followers, setFollowers] = useState<PartialUser[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!artistId) return; 
    const fetchFollowers = async () => {
      setLoading(true);
      try {
        const res = await followApi.getFollowers(artistId);
        setFollowers(res || []);
      } catch (err) {
        console.error("Failed to fetch followers:", err);
        setFollowers([]);
      } finally {
        setLoading(false);
      }
    };
    fetchFollowers();
  }, [artistId]);

  return { followers, loading };
};
