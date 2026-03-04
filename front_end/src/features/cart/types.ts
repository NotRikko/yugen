import { Schema } from "@/types/api-helpers";

export type CartDTO = Schema<"CartDTO">;
export type CartItemDTO = Schema<"CartItemDTO">;
export type CartAddItemDTO = Schema<"CartAddItemDTO">;
export type CartUpdateItemDTO = Schema<"CartUpdateItemDTO">;
export type CheckoutResponseDTO = Schema<"CheckoutResponseDTO">;

export type Pageable = Schema<"Pageable">;