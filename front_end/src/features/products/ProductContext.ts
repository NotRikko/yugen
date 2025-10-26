import { createContext } from "react";
import type { ProductContextType } from "./ProductProvider.types";

export const ProductContext = createContext<ProductContextType | undefined>(undefined);