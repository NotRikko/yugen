import { useState } from "react";
import { cartApi } from "../api/cartApi";
import type { Cart } from "../types/cartTypes";

export const useCartHook = () => {
  const [cart, setCart] = useState<Cart | null>(null);

  const fetchCart = async () => {
    const data = await cartApi.getCart();
    setCart(data);
  };

  const addToCart = async (productId: number, quantity: number) => {
    const updated = await cartApi.addToCart(productId, quantity);
    setCart(updated);
  };

  const updateQuantity = async (cartItemId: number, quantity: number) => {
    if (quantity <= 0) return;
    const updated = await cartApi.updateCartItem(cartItemId, quantity);
    setCart(updated);
  };

  const removeFromCart = async (cartItemId: number) => {
    const updated = await cartApi.removeFromCart(cartItemId);
    setCart(updated);
  };

  const clearCart = async () => {
    const updated = await cartApi.clearCart();
    setCart(updated);
  };

  const totalPrice =
    cart?.items.reduce((sum, item) => sum + item.quantity * (item.product?.price || 0), 0) || 0;

  return { cart, totalPrice, fetchCart, addToCart, updateQuantity, removeFromCart, clearCart };
};