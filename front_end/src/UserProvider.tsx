import { createContext, useState, useEffect, useContext, ReactNode } from "react";

interface User {
    username: string;
    displayName: string;
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
            displayName: "",
            email: "",
            image: ""
        }
    );

    useEffect(() => {
        // Check if there's a saved user in localStorage
        const storedUser = localStorage.getItem("user");
        const storedToken = localStorage.getItem("accessToken");
    
        if (storedUser && storedToken) {
          setUser(JSON.parse(storedUser));
          setIsLoggedIn(true);
        }
      }, []);

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