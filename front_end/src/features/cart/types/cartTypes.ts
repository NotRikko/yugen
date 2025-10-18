import type { Product } from "@/features/user/types/userTypes";

export interface CartItem {
    productId: number;
    quantity: number;
    product: Product;
}

export interface Cart {
    id: number;
    items: CartItem[];
}