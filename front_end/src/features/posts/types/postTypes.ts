import { Artist } from "@/features/artists/types/artistTypes";
import { Product } from "@/features/products/types/productTypes";
import { PartialComment } from "@/features/comments/types/commentTypes";

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
  comments: PartialComment[];
}

export interface PostProps {
  post: Post;
  onSelect?: () => void;
}