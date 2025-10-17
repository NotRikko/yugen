import { useState, useEffect } from "react";
import { cartApi } from "../api/cartApi";
import type { Cart } from "../types/cartTypes";

export const useCartHook = () => {
  const [cart, setCart] = useState<Cart | null>(null);

  const fetchCart = async () => {
    const data = await cartApi.getCart();
    setCart(data);
  };

  const handleAddToCart = async (productId: number, quantity: number) => {
    const updated = await cartApi.addToCart(productId, quantity);
    setCart(updated);
  };

  const handleUpdateQuantity = async (productId: number, quantity: number) => {
    if (quantity <= 0) return;
    const updated = await cartApi.updateCartItem(productId, quantity);
    setCart(updated);
  };

  const handleRemove = async (productId: number) => {
    const updated = await cartApi.removeFromCart(productId);
    setCart(updated);
  };

  const handleClearCart = async () => {
    const updated = await cartApi.clearCart();
    setCart(updated);
  };

  const totalPrice =
    cart?.items.reduce((sum, item) => sum + item.quantity * (item.product?.price || 0), 0) || 0;

  useEffect(() => {
    fetchCart();
  }, []);

  return { cart, fetchCart, handleAddToCart, handleUpdateQuantity, handleRemove, handleClearCart, totalPrice };
};