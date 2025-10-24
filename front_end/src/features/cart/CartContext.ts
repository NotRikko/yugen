import { createContext } from "react";
import type { CartContextType } from "./CartProvider.types";

export const CartContext = createContext<CartContextType | undefined>(undefined);