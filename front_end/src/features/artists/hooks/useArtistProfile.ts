import { useState, useEffect } from "react";
import { artistApi } from "../api/artistApi";
import { postApi } from "@/features/posts/api/postApi";
import { productApi } from "@/features/products/api/productApi";
import type { ArtistDTO } from "../types";
import type { PostDTO } from "@/features/posts/types";
import type { ProductDTO } from "@/features/products/types";

export function useArtistProfile(artistName: string | undefined) {
  const [artist, setArtist] = useState<ArtistDTO | null>(null);
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [products, setProducts] = useState<ProductDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!artistName) return;

    const fetchData = async () => {
      setLoading(true);
      try {
        const artistData = await artistApi.getByUsername(artistName);
        if (!artistData) {
          setArtist(null);
          return;
        }
        setArtist(artistData);

        const [artistPosts, artistProducts] = await Promise.all([
          postApi.getPostsByArtistId(artistData.id!),       
          productApi.getProductsByArtistId(artistData.id!),
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


  return {
    artist,
    posts,
    products,
    loading,
    setPosts,
    setProducts,
  };
}
