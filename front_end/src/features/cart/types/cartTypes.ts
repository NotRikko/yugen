import type { Product } from "@/features/user/types/userTypes";

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