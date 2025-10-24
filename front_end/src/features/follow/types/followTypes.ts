export interface Follow {
    followerId: number;   
    followeeId: number;   
    followedAt: string; 
}
  
export interface FollowStatusResponse {
    isFollowing: boolean;
}

export interface FollowWithDetails extends Follow {
    followerName?: string;
    followeeName?: string;
    followeeAvatarUrl?: string;
}