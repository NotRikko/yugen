import { fetchClient } from "@/shared/api/fetchClient";
import { User, Cart } from "../types/userTypes";
import { Product } from "../types/productTypes";

interface UpdateUserPayload {
  username: string;
  displayName: string;
  email: string;
  file?: File;
}

export const userApi = {
  getCurrentUser: () => fetchClient<User>("/users/me", { method: "GET", auth: true }),

  getCart: () => fetchClient<Cart>("/cart", { method: "GET", auth: true }),

  addToCart: (productId: number, quantity: number) =>
    fetchClient<Cart>(`/cart/add?productId=${productId}&quantity=${quantity}`, {
      method: "POST",
      auth: true,
  }),

  updateUser: async (userId: number, data: UpdateUserPayload): Promise<User> => {
    const formData = new FormData();
    const { file, ...rest } = data;
    formData.append("patch", new Blob([JSON.stringify(rest)], { type: "application/json" }));
    if (file) formData.append("file", file);

    const res = await fetchClient<User>(`/users/update/${userId}`, {
      method: "PATCH",
      body: formData,
      auth: true,
    } as any);

    if (!res) throw new Error("Failed to update user"); 
    return res;
  },

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

  clearCart: () => fetchClient<Cart>("/cart/clear", { method: "POST", auth: true }),
};