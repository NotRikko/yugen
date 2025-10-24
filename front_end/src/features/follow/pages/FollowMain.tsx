import { useState, useEffect} from "react";
import { useFollow } from "../hooks/useFollow";
import { useUser } from "@/features/user/useUserContext";
import type { PartialUser } from "@/features/user/types/userTypes";

type ViewMode = "followers" | "followees";

function FollowMain(): JSX.Element {
  const { getFollowers, getFollowing } = useFollow();
  const { user } = useUser();
  const [viewMode, setViewMode] = useState<ViewMode>("followers");
  const [users, setUsers] = useState<PartialUser[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetch = async () => {
      if (!user) return;
      setLoading(true);
      try {
        const result =
          viewMode === "followers" && user.artistId
            ? await getFollowers(user.artistId)
            : viewMode === "followees"
            ? await getFollowing(user.id)
            : [];
        setUsers(result || []);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
  
    fetch();
  }, [viewMode, user, getFollowers, getFollowing]);

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
      ) : users.length === 0 ? (
        <p>No {viewMode} yet.</p>
      ) : (
        <ul>
          {users.map((u) => (
            <li key={u.displayName} className="flex items-center gap-3 mb-2">
              <img
                src={u.image || "/default-avatar.png"} 
                alt={u.displayName}
                className="w-10 h-10 rounded-full object-cover"
              />
              <span>{u.displayName}</span>
            </li>
          ))}
        </ul>
        )}
    </div>
  );
}

export default FollowMain;