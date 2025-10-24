import { createContext } from "react";
import type { UserContextType } from "./UserProvider.types";

export const UserContext = createContext<UserContextType | undefined>(undefined);