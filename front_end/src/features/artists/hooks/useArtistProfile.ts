import { useState, useEffect } from "react";
import { artistApi } from "../api/artistApi";
import { postApi } from "@/features/posts/api/postApi";
import { productApi } from "@/features/products/api/productApi";
import type { PartialArtist } from "../types/artistTypes";
import type { PostDTO } from "@/features/posts/types/postTypes";
import type { Product } from "@/features/user/types/userTypes";

export function useArtistProfile(artistName: string | undefined) {
  const [artist, setArtist] = useState<PartialArtist | null>(null);
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!artistName) return;

    const fetchData = async () => {
      setLoading(true);
      try {
        const artistData = await artistApi.getByArtistName(artistName);
        if (!artistData) {
          setArtist(null);
          return;
        }
        setArtist(artistData);

        const [artistPosts, artistProducts] = await Promise.all([
          postApi.getPostsByArtistId(artistData.id),
          productApi.getProductsByArtistId(artistData.id),
        ]);

        setPosts(artistPosts ?? []);
        setProducts(artistProducts ?? []);
      } catch (error) {
        console.error("Failed to fetch artist profile:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [artistName]);

  const deletePostOptimistic = async (postId: number) => {
    const postToDelete = posts.find((p) => p.id === postId);
    if (!postToDelete) return;

    setPosts((prev) => prev.filter((p) => p.id !== postId));

    try {
      await postApi.deletePost(postId); 
    } catch (error) {
      console.error("Failed to delete post:", error);
      setPosts((prev) => [postToDelete, ...prev]);
    }
  };

  const deleteProductOptimistic = async (productId: number) => {
    const productToDelete = products.find((p) => p.id === productId);
    if (!productToDelete || !artist) return;

    setProducts((prev) => prev.filter((p) => p.id !== productId));

    try {
      await productApi.deleteProduct(productId);
    } catch (error) {
      console.error("Failed to delete product:", error);
      setProducts((prev) => [productToDelete, ...prev]);
    }
  };

  return {
    artist,
    posts,
    products,
    loading,
    deletePostOptimistic,
    deleteProductOptimistic,
    setPosts,
    setProducts,
  };
}
