import Post from "./Post";
import PostFooter from "./PostFooter";
import { useState, useEffect } from "react";
import { useUser } from "@/features/user/UserProvider";



interface PostDetailsProps {
    post: Post;
  }
  function PostModal({ post }: PostDetailsProps) {
    const { user } = useUser(); 
    const currentUserId = user?.id;
    
    const [isFollowing, setIsFollowing] = useState(false);
    const [loading, setLoading] = useState(false);

    const API_URL = import.meta.env.VITE_API_URL;

    useEffect(() => {
      
      fetch(`${API_URL}/follow/check/${currentUserId}/${post.artist.id}`)
        .then((res) => res.json())
        .then((data) => setIsFollowing(data))
        .catch((err) => console.error(err));
    }, [currentUserId, post.artist.id]);

    const handleFollow = async () => {
      setLoading(true);
      try {
        if (isFollowing) {
          await fetch(`${API_URL}/follow/${currentUserId}/${post.artist.id}`, {
            method: "DELETE",
          });
          setIsFollowing(false);
        } else {
          await fetch(`${API_URL}/follow/${currentUserId}/${post.artist.id}`, {
            method: "POST",
          });
          setIsFollowing(true);
        }
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    return (
      <div className="w-5/6 h-5/6 mx-auto my-6 p-8 border rounded-2xl shadow-xl bg-white overflow-y-auto">
        <div className="flex items-center gap-4 mb-6">
        <img
                src={post.artist.profilePictureUrl || "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg"}
                onError={(e) => {
                    e.currentTarget.src = "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
                }}
                className="w-14 h-14 rounded-full object-cover"
            />
          <p className="text-blue-600 text-lg font-semibold">
            {post.artist.artistName}
          </p>
          <button
            onClick={handleFollow}
            disabled={loading}
            className={`px-3 py-1 rounded-md text-sm font-semibold transition-colors ${
              isFollowing
                ? "bg-gray-200 text-gray-700 hover:bg-gray-300"
                : "bg-blue-600 text-white hover:bg-blue-700"
            }`}
          >
            {loading ? "..." : isFollowing ? "Following" : "Follow"}
          </button>
        </div>
  
        <p className="text-gray-800 text-base mb-6">{post.content}</p>
  
        {post.images?.length > 0 && (
          <div
            className={`
              grid gap-4 mb-8
              ${post.images.length === 1 ? "grid-cols-1" : ""}
              ${post.images.length === 2 ? "grid-cols-2" : ""}
              ${post.images.length >= 3 ? "grid-cols-2 grid-rows-2" : ""}
            `}
          >
            {post.images.map((image, index) => (
              <img
                key={index}
                src={image.url}
                className={`
                  w-full rounded-xl object-cover
                  ${post.images.length >= 3 ? "max-h-[300px]" : "max-h-[600px]"}
                  min-h-[300px]
                `}
                alt={`Post image ${index + 1}`}
              />
            ))}
          </div>
        )}
  
        <PostFooter post={post} />
  
        <div className="mt-8">
          <h3 className="font-semibold text-lg text-gray-700 mb-4">Comments</h3>
          {post.comments.length === 0 ? (
            <p className="text-gray-500 text-base">No comments yet.</p>
          ) : (
            <ul className="space-y-5">
              {post.comments.map((c, i) => (
                <li key={i} className="border-b pb-3">
                  <p className="text-base font-semibold">User {c.userId}</p>
                  <p className="text-gray-700 text-base">{c.content}</p>
                  <p className="text-sm text-gray-400">{c.postedAt}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    );
  }
  

export default PostModal;