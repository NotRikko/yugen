import { useUserHook, guestUser } from "./hooks/useUser";

type UserHook = ReturnType<typeof useUserHook>;

export interface UserContextType {
  user: UserHook["user"];
  setUser: UserHook["setUser"];
  isLoggedIn: UserHook["isLoggedIn"];
  handleLogin: UserHook["handleLogin"];
  handleLogout: UserHook["handleLogout"];
  guestUser: typeof guestUser;
}
