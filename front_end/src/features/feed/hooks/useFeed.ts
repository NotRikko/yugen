import { useState, useEffect, useCallback } from "react";
import { feedApi } from "../api/feedApi";
import type { FeedPostDTO } from "../types";
interface UseFeedProps {
  userFeed?: boolean;
  size?: number;
}

export function useFeed({ userFeed = false, size = 10 }: UseFeedProps = {}) {
  const [posts, setPosts] = useState<FeedPostDTO[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasNext, setHasNext] = useState(false);

  const fetchFeed = useCallback(async () => {
    setLoading(true);

    try {
      const result = userFeed
        ? await feedApi.getUserFeed(page, size)
        : await feedApi.getGlobalFeed(page, size);

      const newPosts = result?.posts ?? [];

      setPosts((prev) =>
        page === 0 ? newPosts : [...prev, ...newPosts]
      );

      setHasNext(result?.hasNext ?? false);
    } catch (error) {
      console.error("Error fetching feed:", error);
    } finally {
      setLoading(false);
    }
  }, [userFeed, page, size]);

  useEffect(() => {
    fetchFeed();
  }, [fetchFeed]);

  const loadMore = () => {
    if (!loading && hasNext) {
      setPage((prev) => prev + 1);
    }
  };

  return { posts, loading, loadMore, page, setPage };
}