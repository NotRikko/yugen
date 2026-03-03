import { fetchClient } from "@/shared/api/fetchClient";
import type { CartDTO } from "../types";

export const cartApi = {
  getCart: async (): Promise<CartDTO> => {
    const cart = await fetchClient<CartDTO>("/cart", { method: "GET", auth: true });
    if (!cart) throw new Error("Cart not found");
    return cart;
  },

  addToCart: async (productId: number, quantity: number): Promise<CartDTO> => {
    const cart = await fetchClient<CartDTO>(
      `/cart/add?productId=${productId}&quantity=${quantity}`,
      { method: "POST", auth: true }
    );
    if (!cart) throw new Error("Failed to add to cart");
    return cart;
  },

  updateCartItem: async (cartItemId: number, quantity: number): Promise<CartDTO> => {
    const cart = await fetchClient<CartDTO>(
      `/cart/update?cartItemId=${cartItemId}&quantity=${quantity}`,
      { method: "PATCH", auth: true }
    );
    if (!cart) throw new Error("Failed to update cart item");
    return cart;
  },

  removeFromCart: async (cartItemId: number): Promise<CartDTO> => {
    const cart = await fetchClient<CartDTO>(
      `/cart/remove?cartItemId=${cartItemId}`,
      { method: "DELETE", auth: true }
    );
    if (!cart) throw new Error("Failed to remove cart item");
    return cart;
  },

  clearCart: async (): Promise<CartDTO> => {
    const cart = await fetchClient<CartDTO>("/cart/clear", { method: "POST", auth: true });
    if (!cart) throw new Error("Failed to clear cart");
    return cart;
  },
};