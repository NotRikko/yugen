import { Artist } from "@/features/artists/types/artistTypes";
import { Product } from "@/features/products/types/productTypes";

export interface Comment {
  userId: number;
  content: string;
  postedAt: string;
}

export interface Like {
  userId: number;
  likedAt: string;
}

export interface Image {
  imageId: number;
  url: string;
}

export interface Post {
  id: number;
  content: string;
  artist: Artist;
  product?: Product;
  likes: Like[];
  images: Image[];
  comments: Comment[];
}

export interface PostProps {
  post: Post;
  onSelect?: () => void;
}