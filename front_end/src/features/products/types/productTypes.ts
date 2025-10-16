import { Artist } from "@/features/artists/types/artistTypes";

export interface Series {
  id: number;
}

export interface Collection {
  id: number;
}

export interface PartialProduct {
  id: number;
  name: string;
  description?: string;
  price?: number;
  image?: string;
}

export interface Product extends PartialProduct {
  artist: Artist;
  series: Series[];
  collections: Collection[];
  quantityInStock: number;
}