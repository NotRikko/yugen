import { usePost } from "@/features/posts/hooks/usePost";
import { artistApi } from "../api/artistApi";
import { useEffect, useState } from "react";
import { Product } from "@/features/user/types/userTypes";

export default function useArtistDashboard(artistId: number) {
  const { posts, setPosts, createPostOptimistic, deletePostOptimistic } = usePost();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPostsAndProducts = async () => {
      try {
        const [fetchedPosts, fetchedProducts] = await Promise.all([
          artistApi.getPostsById(artistId),
          artistApi.getProducts(artistId),
        ]);

        setPosts(fetchedPosts ?? []);
        setProducts(fetchedProducts ?? []);
      } catch (error) {
        console.error("Failed to fetch artist data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPostsAndProducts();
  }, [artistId, setPosts]);

  return { posts, products, loading, createPostOptimistic, deletePostOptimistic };
}