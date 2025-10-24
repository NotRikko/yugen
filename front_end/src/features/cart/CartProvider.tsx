import { createContext, ReactNode, useContext } from "react";
import { useCartHook } from "./hooks/useCart";

interface CartContextType {
  cart: ReturnType<typeof useCartHook>["cart"];
  totalPrice: ReturnType<typeof useCartHook>["totalPrice"];
  fetchCart: ReturnType<typeof useCartHook>["fetchCart"];
  addToCart: ReturnType<typeof useCartHook>["addToCart"];
  updateQuantity: ReturnType<typeof useCartHook>["updateQuantity"];
  removeFromCart: ReturnType<typeof useCartHook>["removeFromCart"];
  clearCart: ReturnType<typeof useCartHook>["clearCart"];
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const cart = useCartHook();

  return (
    <CartContext.Provider
      value={{
        cart: cart.cart,
        totalPrice: cart.totalPrice,
        fetchCart: cart.fetchCart,
        addToCart: cart.addToCart,
        updateQuantity: cart.updateQuantity,
        removeFromCart: cart.removeFromCart,
        clearCart: cart.clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart must be used within a CartProvider");
  return context;
};