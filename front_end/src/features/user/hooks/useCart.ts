import { useUserHook } from "./useUser";
import { userApi } from "../api/userApi";

export const useCartHook = () => {
  const { cart, setCart } = useUserHook();

  const handleUpdateQuantity = async (productId: number, quantity: number) => {
    if (quantity <= 0) return;
    const updated = await userApi.updateCartItem(productId, quantity);
    setCart(updated);
  };

  const handleRemove = async (productId: number) => {
    const updated = await userApi.removeFromCart(productId);
    setCart(updated);
  };

  const handleAddToCart = async (productId: number, quantity: number) => {
    const updated = await userApi.addToCart(productId, quantity);
    setCart(updated);
  };

  const handleClearCart = async () => {
    const updated = await userApi.clearCart();
    setCart(updated);
  };

  const totalPrice =
    cart?.items.reduce((sum, item) => sum + item.quantity * (item.product?.price || 0), 0) || 0;

  return { cart, handleUpdateQuantity, handleRemove, handleAddToCart, handleClearCart, totalPrice };
};