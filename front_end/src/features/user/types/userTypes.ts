export interface Product {
  id: number;
  name: string;
  price: number;
  image?: string;
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

export interface PartialUser {
  displayName: string;
  image: string;
}

