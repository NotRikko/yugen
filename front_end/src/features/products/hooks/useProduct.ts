import { useState, useEffect } from "react";
import { productApi } from "../api/productApi";
import { useUser } from "@/features/user/UserProvider";
import type { Product } from "../types/productTypes";

export const useProductHook = () => {
    const { user } = useUser();
    const [products, setProducts] = useState<Product[]>([]);

    const fetchProducts = async () => {
        const data = await productApi.fetchCurrentArtistProducts();
        setProducts(data);
    }

    useEffect(() => {
        if (!user.artistId) return;
        fetchProducts();
    }, [user.artistId]);

    return { products, fetchProducts }
}