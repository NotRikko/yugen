import {  ReactNode } from "react";
import { UserContext } from "./UserContext";
import { useUserHook, guestUser } from "./hooks/useUser";




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
