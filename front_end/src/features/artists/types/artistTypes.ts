export interface PartialArtist {
  id: number;
  artistName: string;
  bio?: string;
  profilePictureUrl?: string;
  bannerPictureUrl?: string;
  postsCount?: number;
}

export interface ArtistUser {
  id: number;
  username: string;
  displayName: string;
  email: string;
  image: string;
}

export interface Artist extends PartialArtist {
  user: ArtistUser;
}