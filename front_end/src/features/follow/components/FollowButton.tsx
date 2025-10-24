import { useEffect, useState } from "react";
import { useFollow } from "../hooks/useFollow";
import { Button } from "@/shared/ui/button";

interface FollowButtonProps {
  artistId: number;
  onChange?: (isFollowing: boolean) => void; 
}

export function FollowButton({ artistId, onChange }: FollowButtonProps) {
  const { followArtist, unfollowArtist, checkIfFollowing } = useFollow();
  const [isFollowing, setIsFollowing] = useState<boolean | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadFollowState = async () => {
      const status = await checkIfFollowing(artistId);
      setIsFollowing(status);
    };
    loadFollowState();
  }, [artistId, checkIfFollowing]);

  const handleToggleFollow = async () => {
    if (isFollowing === null) return;
    setLoading(true);

    const newState = !isFollowing;
    setIsFollowing(newState);

    try {
      if (newState) {
        await followArtist(artistId);
      } else {
        await unfollowArtist(artistId);
      }
      onChange?.(newState);
    } catch (err) {
      setIsFollowing(isFollowing);
      console.error("Failed to toggle follow:", err);
    } finally {
      setLoading(false);
    }
  };

  if (isFollowing === null) return null; 

  return (
    <Button
      variant={isFollowing ? "outline" : "default"}
      onClick={handleToggleFollow}
      disabled={loading}
    >
      {loading ? "..." : isFollowing ? "Unfollow" : "Follow"}
    </Button>
  );
}