
export interface FullArtist {
    id: string;
    username: string;
    displayName: string;
    email: string;         
    bio?: string;
    profilePictureUrl?: string;
    bannerPictureUrl? : string;
    postsCount: number;
    createdAt: string;
    updatedAt: string;
  }
  
  export type PartialArtist = Pick<
    FullArtist,
    "id" | "username" | "bio" | "profilePictureUrl" | "bannerPictureUrl" | "postsCount"
  >;