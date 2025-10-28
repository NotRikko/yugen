import { useState, useEffect, useCallback } from "react";
import { feedApi } from "../api/feedApi";
import { usePost } from "@/features/posts/hooks/usePost";
interface UseFeedProps {
  userFeed?: boolean;
  size?: number;
}

export function useFeed({ userFeed = false, size = 10 }: UseFeedProps = {}) {
  const { posts, setPosts, createPostOptimistic, deletePostOptimistic } = usePost();
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchFeed = useCallback(async () => {
    setLoading(true);
    try {
      const result = userFeed
        ? await feedApi.getUserFeed(page, size)
        : await feedApi.getGlobalFeed(page, size);

      setPosts((prev) => (page === 0 ? result || [] : [...prev, ...(result || [])]));
    } catch (error) {
      console.error("Error fetching feed:", error);
    } finally {
      setLoading(false);
    }
  }, [userFeed, page, size, setPosts]);

  useEffect(() => {
    fetchFeed();
  }, [fetchFeed]);

  const loadMore = () => setPage((prev) => prev + 1);

  return { posts, loading, createPostOptimistic, deletePostOptimistic, loadMore, page, setPage };
}