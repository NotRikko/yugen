import { Button } from "@/shared/ui/button";
import { useFollow } from "../hooks/useFollow";
interface FollowButtonProps {
  artistId: number;
}

export function FollowButton({ artistId}: FollowButtonProps) {
  const { isFollowing, loading, toggleFollow } = useFollow(artistId);

  if (isFollowing === null) return null;

  return (
    <Button
      variant={isFollowing ? "outline" : "default"}
      disabled={loading}
      onClick={toggleFollow}
    >
      {loading ? "..." : isFollowing ? "Unfollow" : "Follow"}
    </Button>
  );
}