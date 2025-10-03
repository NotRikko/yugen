import { createContext, useState, useEffect, useContext, ReactNode } from "react";

interface Product {
    id: number;
    name: string;
    price: number;
    image?: string;
}

interface CartItem {
    productId: number;
    quantity: number;
    product: Product;
}

interface Cart {
    id: number;
    items: CartItem[];
    }

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
    cart: Cart | null;
    setCart: React.Dispatch<React.SetStateAction<Cart | null>>;
    addToCart: (productId: number, quantity: number) => Promise<void>;
    updateCartItem: (productId: number, quantity: number) => Promise<void>;
    removeFromCart: (productId: number) => Promise<void>;
    clearCart: () => Promise<void>;
    fetchCart: () => Promise<void>;
    mergeGuestCart: (guestCart: Cart) => Promise<void>;
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
    const [cart, setCart] = useState<Cart | null>(null);

    const API_URL = import.meta.env.VITE_API_URL;

    useEffect(() => {

        const storedUser = localStorage.getItem("user");
        const storedToken = localStorage.getItem("accessToken");
    
        if (storedUser && storedToken) {
            const parsedUser = JSON.parse(storedUser);
            setUser({
                ...parsedUser,
                artistId: parsedUser.artistId ?? null,
            });
            setIsLoggedIn(true);
    
            fetchCart(storedToken);
        }
    }, []);

    const fetchCart = async (tokenParam?: string) => {
        try {
            const token = tokenParam || localStorage.getItem("accessToken");
            if (!token) return;
    
            const res = await fetch(`${API_URL}/cart`, {
                method: "GET",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });
    
            if (res.ok) {
                const data = await res.json();
                setCart(data);
                console.log("Fetched cart:", data);
            } else {
                console.error("Failed to fetch cart:", res.statusText);
            }
        } catch (err) {
            console.error("Failed to fetch cart:", err);
        }
    };

    const addToCart = async (productId: number, quantity: number) => {
        try {
            const token = localStorage.getItem("accessToken");
            const res = await fetch(`${API_URL}/cart/add?productId=${productId}&quantity=${quantity}`, {
                method: "POST",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });
    
            if (res.ok) {
                const updatedCart = await res.json();
                setCart(updatedCart); 
                console.log("Cart updated:", updatedCart);
            } else {
                const errData = await res.json();
                console.error("Failed to add to cart:", errData);
            }
        } catch (err) {
            console.error("Failed to add to cart:", err);
        }
    };

    const updateCartItem = async (productId: number, quantity: number) => {
        try {
            const res = await fetch(`${API_URL}/cart/update?productId=${productId}&quantity=${quantity}`, {
                method: "PATCH",
                credentials: "include",
            });
            if (res.ok) {
                await fetchCart();
            }
        } catch (err) {
            console.error("Failed to update cart item:", err);
        }
    };

    const removeFromCart = async (productId: number) => {
        try {
            const res = await fetch(`${API_URL}/cart/remove?productId=${productId}`, {
                method: "DELETE",
                credentials: "include",
            });
            if (res.ok) {
                await fetchCart();
            }
        } catch (err) {
            console.error("Failed to remove from cart:", err);
        }
    };

    const clearCart = async () => {
        try {
            const res = await fetch(`${API_URL}/cart/clear`, { method: "POST", credentials: "include" });
            if (res.ok) {
                setCart({ id: cart?.id || 0, items: [] });
            }
        } catch (err) {
            console.error("Failed to clear cart:", err);
        }
    };

    const mergeGuestCart = async (guestCart: Cart) => {
        try {
            const res = await fetch(`${API_URL}/cart/merge`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify(guestCart),
            });
            if (res.ok) {
                await fetchCart();
            }
        } catch (err) {
            console.error("Failed to merge guest cart:", err);
        }
    };

    return (
        <UserContext.Provider
            value={{
                isLoggedIn,
                setIsLoggedIn,
                user,
                guestUser,
                setUser,
                cart,
                setCart,
                addToCart,
                updateCartItem,
                removeFromCart,
                clearCart,
                fetchCart,
                mergeGuestCart,
            }}
        >
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