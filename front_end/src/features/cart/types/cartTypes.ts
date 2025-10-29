import type { Product } from "@/features/products/types/productTypes";
export interface CartItem {
    id: number;
    productId: number;
    quantity: number;
    product: Product;
    image: string;
}

export interface Cart {
    id: number;
    items: CartItem[];
}