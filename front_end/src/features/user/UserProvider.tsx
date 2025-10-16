import { createContext, ReactNode, useContext } from "react";
import { useUserHook } from "./hooks/useUser";
import { useCartHook } from "./hooks/useCart";

type UserHook = ReturnType<typeof useUserHook>;
type CartHook = ReturnType<typeof useCartHook>;

interface UserContextType {
  user: UserHook["user"];
  setUser: UserHook["setUser"];
  isLoggedIn: UserHook["isLoggedIn"];
  handleLogin: UserHook["handleLogin"];
  handleLogout: UserHook["handleLogout"];
  cart: CartHook["cart"];
  handleAddToCart: CartHook["handleAddToCart"];
  handleUpdateQuantity: CartHook["handleUpdateQuantity"];
  handleRemove: CartHook["handleRemove"];
  handleClearCart: CartHook["handleClearCart"];
  totalPrice: CartHook["totalPrice"];
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface Props {
  children: ReactNode;
}

export const UserProvider = ({ children }: Props) => {
  const user = useUserHook();
  const cart = useCartHook();

  return (
    <UserContext.Provider
      value={{
        user: user.user,
        setUser: user.setUser,
        isLoggedIn: user.isLoggedIn,
        handleLogin: user.handleLogin,
        handleLogout: user.handleLogout,
        cart: cart.cart,
        handleAddToCart: cart.handleAddToCart,
        handleUpdateQuantity: cart.handleUpdateQuantity,
        handleRemove: cart.handleRemove,
        handleClearCart: cart.handleClearCart,
        totalPrice: cart.totalPrice,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within a UserProvider");
  return context;
};