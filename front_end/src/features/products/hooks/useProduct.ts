import { useEffect, useState } from "react";
import { productApi } from "../api/productApi";
import { useUser } from "@/features/user/useUserContext";
import type { Product, ProductCreate } from "../types/productTypes";

export const useProductHook = () => {
  const { user } = useUser();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError(null);

      const data = await productApi.fetchCurrentArtistProducts();
      setProducts(data);
    } catch (err) {
      console.error("Failed to fetch products:", err);
      setError("Failed to load products.");
    } finally {
      setLoading(false);
    }
  };

  const createProductOptimistic = async (newProduct: ProductCreate) => {
    const tempId = Date.now();
  
    const optimisticProduct: Product = {
        id: tempId,
        name: newProduct.name,
        description: newProduct.description ?? "",
        price: newProduct.price,
        quantityInStock: newProduct.quantityInStock,
        images: [],
        artist: {
          id: user.artistId!,
          user: user,
          artistName: user.displayName ?? "Unknown Artist", 
        },
        series: [],
        collections: [],
      };
  
    setProducts(prev => [optimisticProduct, ...prev]);
  
    try {
      const savedProduct = await productApi.createProduct({
        artistId: user?.artistId ?? null,
        name: newProduct.name,
        description: newProduct.description ?? "", 
        price: newProduct.price,
        quantityInStock: newProduct.quantityInStock,
        seriesIds: newProduct.seriesIds ?? [],
        collectionIds: newProduct.collectionIds ?? [],
        files: newProduct.files,
      });
  
      setProducts(prev =>
        prev.map(p => (p.id === tempId ? savedProduct : p))
      );
    } catch (err) {
      setProducts(prev => prev.filter(p => p.id !== tempId));
      console.error("Failed to create product:", err);
      setError("Failed to create product.");
    }
  };

  const deleteProductOptimistic = async (productId: number) => {
    const previous = [...products];
    setProducts(prev => prev.filter(p => p.id !== productId));

    try {
      await productApi.deleteProduct(productId);
    } catch (err) {
      setProducts(previous); 
      console.error("Failed to delete product:", err);
      setError("Failed to delete product.");
    }
  };

  useEffect(() => {
    if (!user?.artistId) return;
    fetchProducts();
  }, [user?.artistId]);

  return {
    products,
    loading,
    error,
    fetchProducts,
    createProductOptimistic,
    deleteProductOptimistic,
  };
};