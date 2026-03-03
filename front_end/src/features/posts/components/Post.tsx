import PostFooter from "./PostFooter";
import { ReusableDropdown } from "@/shared/ReusableDropdown";
import type { FeedPostDTO } from "@/features/feed/types";
import { useNavigate } from "react-router-dom";
import { useUser } from "@/features/user/useUserContext";
interface PostProps {
  post: FeedPostDTO;
  onSelect?: () => void;
  onUpdate: (postId: number, content: string) => void; 
  onDelete: () => void;
}

function Post({ post, onSelect, onUpdate, onDelete }: PostProps) {
  const navigate = useNavigate();
  const { user } = useUser();

  const postMenuItems = [
    {
      label: "Delete",
      onClick: onDelete,
    },
    {
      label: "Edit",
      onClick: () => onUpdate
    }
  ];

  const handleArtistClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (post.artist?.artistName) {
      navigate(`/artist/${post.artist.artistName}`);
    }
  };

  const images = post.imageUrls || [];

  return (
    <div className="w-5/6 mx-auto p-8 border rounded-lg shadow-md bg-white max-h-[800px]">
      <div onClick={onSelect} className="cursor-pointer">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <img
              onClick={handleArtistClick}
              src={
                post.artist?.profilePictureUrl ||
                "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg"
              }
              onError={(e) => {
                e.currentTarget.src =
                  "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
              }}
              className="w-10 h-10 rounded-full object-cover"
              alt="Artist profile picture."
            />
            <p
              onClick={handleArtistClick}
              className="text-blue-500 text-sm font-semibold"
            >
              {post.artist?.artistName || "Unknown Artist"}
            </p>
          </div>

          {post.artist?.id === user.id && (
            <ReusableDropdown
              triggerText="•••"
              items={postMenuItems} 
            />
          )}
        </div>

        <p className="text-gray-800 text-sm my-2">{post.content || ""}</p>
      </div>

      {images.length > 0 && (
        <div
          className={`grid gap-2 my-4 ${
            images.length === 1
              ? ""
              : images.length === 2
              ? "grid-cols-2"
              : "grid-cols-2 md:grid-cols-3"
          }`}
        >
          {images.map((url, index) => (
            <img
              key={index}
              src={url}
              alt={`Post image ${index + 1}`}
              className={`w-full rounded-md ${
                images.length === 1
                  ? "max-h-[500px] object-contain"
                  : "h-48 object-cover"
              }`}
              onError={(e) => {
                e.currentTarget.src =
                  "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
              }}
            />
          ))}
        </div>
      )}

      <PostFooter post={post} />
    </div>
  );
}

export default Post;