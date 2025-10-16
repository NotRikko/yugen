import { useUser } from "@/features/user/UserProvider";

export const useCart = () => {
  const { cart, updateCartItem, removeFromCart } = useUser();

  const handleUpdateQuantity = (productId: number, newQuantity: number) => {
    if (newQuantity <= 0) return;
    updateCartItem(productId, newQuantity);
  };

  const handleRemove = (productId: number) => {
    removeFromCart(productId);
  };

  const totalPrice = cart?.items.reduce((sum, item) => sum + item.quantity * (item.product?.price || 0), 0) || 0;

  return {
    cart,
    handleUpdateQuantity,
    handleRemove,
    totalPrice,
  };
};