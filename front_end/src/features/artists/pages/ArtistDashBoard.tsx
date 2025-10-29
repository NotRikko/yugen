import { usePost } from "@/features/posts/hooks/usePost";
import { postApi } from "@/features/posts/api/postApi";
import { productApi } from "@/features/products/api/productApi";
import { useEffect, useState } from "react";
import type { Product } from "@/features/products/types/productTypes";

export default function useArtistDashboard(artistId: number) {
  const { posts, setPosts, createPostOptimistic, deletePostOptimistic } = usePost();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPostsAndProducts = async () => {
      try {
        const [fetchedPosts, fetchedProducts] = await Promise.all([
          postApi.getPostsByArtistId(artistId),
          productApi.getProductsByArtistId(artistId),
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