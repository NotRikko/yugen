import { ReactNode, useEffect } from "react";
import { ProductContext } from "./ProductContext";
import { useProductHook } from "./hooks/useProduct";
import { useUser } from "../user/useUserContext";

export const ProductProvider = ({ children }: { children: ReactNode }) => {
  const { user } = useUser();
  const product = useProductHook();

  useEffect(() => {
    if (user?.artistId) {
      product.fetchProducts();
    }
  }, [user?.artistId, product]);

  return (
    <ProductContext.Provider value={product}>
      {children}
    </ProductContext.Provider>
  );
};