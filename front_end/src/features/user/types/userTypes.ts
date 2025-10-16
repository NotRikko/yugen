export interface Product {
    id: number;
    name: string;
    price: number;
    image?: string;
  }
  
  export interface CartItem {
    productId: number;
    quantity: number;
    product: Product;
  }
  
  export interface Cart {
    id: number;
    items: CartItem[];
  }
  
  export interface User {
    id: number;
    username: string;
    displayName: string;
    email: string;
    image: string;
    artistId: number | null;
    isGuest: boolean;
  }

