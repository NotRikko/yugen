import { useCartHook } from "./hooks/useCart";

export interface CartContextType {
  cart: ReturnType<typeof useCartHook>["cart"];
  totalPrice: ReturnType<typeof useCartHook>["totalPrice"];
  fetchCart: ReturnType<typeof useCartHook>["fetchCart"];
  addToCart: ReturnType<typeof useCartHook>["addToCart"];
  updateQuantity: ReturnType<typeof useCartHook>["updateQuantity"];
  removeFromCart: ReturnType<typeof useCartHook>["removeFromCart"];
  clearCart: ReturnType<typeof useCartHook>["clearCart"];
}