import { createContext, ReactNode, useContext } from "react";
import { useUserHook, guestUser } from "./hooks/useUser";

type UserHook = ReturnType<typeof useUserHook>;

interface UserContextType {
  user: UserHook["user"];
  setUser: UserHook["setUser"];
  isLoggedIn: UserHook["isLoggedIn"];
  handleLogin: UserHook["handleLogin"];
  handleLogout: UserHook["handleLogout"];
  guestUser: typeof guestUser;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const user = useUserHook();

  return (
    <UserContext.Provider
      value={{
        user: user.user ?? guestUser,
        setUser: user.setUser,
        isLoggedIn: user.isLoggedIn,
        handleLogin: user.handleLogin,
        handleLogout: user.handleLogout,
        guestUser,
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