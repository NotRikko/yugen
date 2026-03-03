import { fetchClient } from "@/shared/api/fetchClient";
import type { ProductDTO } from "../types";

export const productApi = {
    createProduct: async (data: {
        name: string;
        quantityInStock: number;
        description: string;
        price: number;
        seriesIds: number[];
        collectionIds: number[];
        files?: File[];
      }): Promise<ProductDTO> => {
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
          new Blob([JSON.stringify(productData)], { type: "application/json" })
        );
      
        (data.files ?? []).forEach((file) => formData.append("files", file));
      
        const result = await fetchClient<ProductDTO>("/products/", {
          method: "POST",
          body: formData,
          auth: true,
        });
      
        if (!result) throw new Error("Failed to create product");
        return result;
    },

    fetchCurrentArtistProducts: async () : Promise<ProductDTO[]> => {
        const products = await fetchClient<ProductDTO[]>("/artists/me/products", {
            method: "GET",
            auth: true
        });

        if (!products) throw new Error("Products not found");
        return products;
    },

    getProductById: ( id: number) =>
        fetchClient<ProductDTO>(`products/${id}`, { method: "GET" }),

    getProductsByArtistId: (artistId: number) =>
        fetchClient<ProductDTO[]>(`/artists/${artistId}/products`, { method: "GET" }),
    
    deleteProduct: async (productId: number) : Promise<void> => {
        await fetchClient<void>(`/products/${productId}`, {
            method: "DELETE",
            auth: true,
        });
    }
}