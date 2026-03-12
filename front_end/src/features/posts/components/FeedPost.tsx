import type { FeedPostDTO } from "@/features/feed/types";

interface FeedPostProps {
  post: FeedPostDTO;
}

function FeedPost({ post }: FeedPostProps) {
  const imageUrl = post.imageUrls?.[0];

  return (
    <div className="bg-white rounded-2xl shadow-sm overflow-hidden hover:shadow-md transition-shadow duration-200">
      
      <div className="flex items-center gap-3 p-3">
        <img
          src={post.artist?.profilePictureUrl || "/default-avatar.png"}
          alt={post.artist?.displayName}
          className="w-8 h-8 rounded-full object-cover"
        />
        <span className="text-sm font-semibold text-gray-800 truncate">
          {post.artist?.displayName}
        </span>
      </div>

      {imageUrl && (
        <div className="w-full">
          <img
            src={imageUrl}
            alt="Post content"
            className="w-full object-cover"
            loading="lazy"
          />
        </div>
      )}
    </div>
  );
}

export default FeedPost;