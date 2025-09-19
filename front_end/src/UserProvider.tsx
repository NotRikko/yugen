import { createContext, useState, useEffect, useContext, ReactNode } from "react";

interface User {
    id: number;
    username: string;
    displayName: string;
    email: string;
    image: string;
    artistId: number | null;
    isGuest: boolean;
}

interface UserContextType {
    isLoggedIn: boolean;
    setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
    user: User;
    guestUser: User;
    setUser: React.Dispatch<React.SetStateAction<User>>;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
    children: ReactNode; 
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
    const guestUser: User = {
        id: 0,
        username: "Guest",
        displayName: "Guest User",
        email: "",
        image: "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg",
        artistId: null,
        isGuest: true
      };
    
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState<User>(guestUser);


    useEffect(() => {
        // Check if there's a saved user in localStorage
        const storedUser = localStorage.getItem("user");
        const storedToken = localStorage.getItem("accessToken");
    
        if (storedUser && storedToken) {
            const parsedUser = JSON.parse(storedUser);
            setUser({
                ...parsedUser,
                artistId: parsedUser.artistId ?? null, 
            });
            setIsLoggedIn(true);
        }
      }, []);

    return (
        <UserContext.Provider value={{ isLoggedIn, user, guestUser, setUser, setIsLoggedIn }}>
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