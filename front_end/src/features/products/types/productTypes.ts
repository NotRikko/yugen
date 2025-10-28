  import type { Artist } from "@/features/artists/types/artistTypes";
  import type { ImageDTO } from "@/features/posts/types/postTypes";

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
    price: number;
    images?: ImageDTO[];
  }

  export interface Product extends PartialProduct {
    artist: Artist;
    series: Series[];
    collections: Collection[];
    quantityInStock: number;
  }

  export interface ProductCreate {
    name: string;
    description?: string;
    price: number;
    quantityInStock: number;
    seriesIds: number[];       
    collectionIds: number[];  
    files?: File[];
    artistId?: number | null;
  }