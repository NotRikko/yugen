import { fetchClient } from "@/shared/api/fetchClient";
import type { Cart } from "../types/cartTypes";

export const cartApi = {
  getCart: () => fetchClient<Cart>("/cart", { method: "GET", auth: true }),

  addToCart: (productId: number, quantity: number) =>
    fetchClient<Cart>(`/cart/add?productId=${productId}&quantity=${quantity}`, {
      method: "POST",
      auth: true,
    }),

  updateCartItem: (productId: number, quantity: number) =>
    fetchClient<Cart>(`/cart/update?productId=${productId}&quantity=${quantity}`, {
      method: "PATCH",
      auth: true,
    }),

  removeFromCart: (productId: number) =>
    fetchClient<Cart>(`/cart/remove?productId=${productId}`, {
      method: "DELETE",
      auth: true,
    }),

  clearCart: () =>
    fetchClient<Cart>("/cart/clear", { method: "POST", auth: true }),
};