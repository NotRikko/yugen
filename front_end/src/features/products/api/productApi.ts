import { fetchClient } from "@/shared/api/fetchClient";
import type { Product } from "../types/productTypes";

export const productApi = {
    createProduct: async (data: {
        artistId: number | null;
        name: string;
        quantityInStock: number;
        description: string;
        price: number;
        seriesIds: number[];
        collectionIds: number[];
        files?: File[];
    }) : Promise<Product> => {
        const formData = new FormData();

        const productData = {
            name: data.name,
            description: data.description,
            price: data.price,
            quantityInStock: data.quantityInStock,
            seriesIds: data.seriesIds ?? [],
            collectionIds: data.collectionIds ?? [],
        };

        formData.append(
            "product",
            new Blob([JSON.stringify(productData)], { type: "application/json"})
        );

        if (data.files && data.files.length > 0) {
            data.files.forEach((file) => formData.append("files", file));
        }

        const result = await fetchClient<Product>("/products/" , {
            method: "POST",
            body: formData,
            auth: true,
        });

        if (!result) throw new Error("Failed to create product");

        return result
    },

    fetchCurrentArtistProducts: async () : Promise<Product[]> => {
        const products = await fetchClient<Product[]>("/artists/me/products", {
            method: "GET",
            auth: true
        });

        if (!products) throw new Error("Products not found");
        return products;
    },

    getProductsByArtistId: (artistId: number) =>
        fetchClient<Product[]>(`/artists/${artistId}/products`, { method: "GET" }),
    
    deleteProduct: async (productId: number) : Promise<void> => {
        await fetchClient<void>(`/products/${productId}`, {
            method: "DELETE",
            auth: true,
        });
    }
}