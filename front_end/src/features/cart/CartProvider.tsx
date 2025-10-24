import { ReactNode, useEffect } from "react";
import { CartContext } from "./CartContext";
import { useCartHook } from "./hooks/useCart";
import { useUser } from "../user/useUserContext";

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const { isLoggedIn } = useUser();
  const cart = useCartHook();

  useEffect(() => {
    if (isLoggedIn) cart.fetchCart();
  }, [cart, isLoggedIn]);

  return <CartContext.Provider value={cart}>{children}</CartContext.Provider>;
};