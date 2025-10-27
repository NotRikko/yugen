import { useState } from "react";
import { useFollowers } from "../hooks/useFollowers";
import { useFollowing } from "../hooks/useFollowing";
import { useUser } from "@/features/user/useUserContext";

type ViewMode = "followers" | "followees";

interface DisplayUser {
  id: number;
  name: string;
  avatarUrl?: string;
}

function FollowDashBoard(): JSX.Element {
  const { user } = useUser();
  const [viewMode, setViewMode] = useState<ViewMode>("followers");

  const { followers, loading: loadingFollowers } = useFollowers(user?.artistId ?? undefined);
  const { following, loading: loadingFollowing } = useFollowing();

  
  const usersToDisplay: DisplayUser[] =
  viewMode === "followers"
    ? followers.map(f => ({
        id: f.id,
        name: f.displayName,
        avatarUrl: f.image,
      }))
    : following.map(f => ({
        id: f.id,
        name: f.artistName,
        avatarUrl: f.profilePictureUrl,
      }));
      
  const loading =
    viewMode === "followers" ? loadingFollowers : loadingFollowing;

  return (
    <div className="p-12">
      <div className="flex justify-center gap-4 mb-4">
        <button
          className={`px-4 py-2 rounded ${
            viewMode === "followers" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
          onClick={() => setViewMode("followers")}
        >
          Followers
        </button>
        <button
          className={`px-4 py-2 rounded ${
            viewMode === "followees" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
          onClick={() => setViewMode("followees")}
        >
          Following
        </button>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : usersToDisplay.length === 0 ? (
        <p>No {viewMode} yet.</p>
      ) : (
        <ul>
          {usersToDisplay.map(u => (
            <li key={u.id} className="flex items-center gap-3 mb-2">
              <img
                src={u.avatarUrl || "/default-avatar.png"}
                alt={u.name}
                className="w-10 h-10 rounded-full object-cover"
              />
              <span>{u.name}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default FollowDashBoard;