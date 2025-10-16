export const tokenService = {
    get: () => localStorage.getItem("accessToken"),
    set: (token: string) => localStorage.setItem("accessToken", token),
    remove: () => localStorage.removeItem("accessToken"),
  };