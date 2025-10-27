import { useState, useEffect, useCallback } from "react";
import type { PostDTO } from "@/features/posts/types/postTypes";
import { useUser } from "@/features/user/useUserContext";
import { postApi } from "@/features/posts/api/postApi";
import { feedApi } from "../api/feedApi";
interface UseFeedProps {
  userFeed?: boolean;
  size?: number;
}

export function useFeed({ userFeed = false, size = 10 }: UseFeedProps = {}) {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState<boolean>(true);
  const { user } = useUser();

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
  }, [userFeed, page, size]);

  useEffect(() => {
    fetchFeed();
  }, [fetchFeed]);

  const createPostOptimistic = async (newPostData: {
    content: string;
    files?: File[];
  }) => {
    if (!user || user.isGuest) return;

    const tempPost: PostDTO = {
        id: Date.now(),
        content: newPostData.content,
        images: [],
        artist: {
          id: user.artistId ?? 0,
          artistName: user.displayName, 
          profilePictureUrl: user.image,
          user: {
            id: user.id,
            username: user.username,
            displayName: user.displayName,
            email: user.email,
            image: user.image,
          },
        },
        likes: [],
        comments: [],
      };

    setPosts((prev) => [tempPost, ...prev]);

    try {
      const createdPost = await postApi.createPost({
        artistId: user.artistId,
        content: newPostData.content,
        files: newPostData.files,
      });

      setPosts((prev) => prev.map((p) => (p.id === tempPost.id ? createdPost : p)));
    } catch (error) {
      setPosts((prev) => prev.filter((p) => p.id !== tempPost.id));
      console.error("Failed to create post:", error);
    }
  };

  const loadMore = () => setPage((prev) => prev + 1);

  return { posts, loading, createPostOptimistic, loadMore, page, setPage };
}