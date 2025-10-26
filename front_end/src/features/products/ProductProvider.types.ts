import { useProductHook } from "./hooks/useProduct";

export interface ProductContextType {
    products: ReturnType<typeof useProductHook>["products"];
    fetchProducts: ReturnType<typeof useProductHook>["fetchProducts"];
}