import { Artist } from "@/features/artists/types/artistTypes";
import { Product } from "@/features/products/types/productTypes";
import { PartialComment } from "@/features/comments/types/commentTypes";

export interface PostDTO {
  id: number;
  content: string;
  artist: Artist;
  product?: Product;
  likes: LikeDTO[];
  images: ImageDTO[];
  comments: PartialComment[];
}

export interface LikeDTO {
  userId: number;
  likedAt: string;
}

export interface ImageDTO {
  imageId: number;
  url: string;
}

export interface PostProps {
  post: PostDTO;
  onSelect?: () => void;
}