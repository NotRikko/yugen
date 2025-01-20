import { createContext, useState, useContext, ReactNode } from "react";

interface User {
    username: string;
    name: string;
    email: string;
    image: string;
}

interface UserContextType {
    isLoggedIn: boolean;
    setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
    user: User;
    setUser: React.Dispatch<React.SetStateAction<User>>;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
    children: ReactNode; 
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState<User>(
        {
            username : "",
            name: "",
            email: "",
            image: ""
        }
    );

    return (
        <UserContext.Provider value={{ isLoggedIn, user, setUser, setIsLoggedIn }}>
            {children}
        </UserContext.Provider>
    );

};

export const useUser = (): UserContextType => {
    const context = useContext(UserContext);
    if (!context) {
      throw new Error("useUser must be used within a UserProvider");
    }
    return context;
  };