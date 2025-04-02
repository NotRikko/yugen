import { Heart, MessageCircle, ShoppingBag, Share2 } from "lucide-react"
import { useState } from "react"

interface Like {
    userId: number;
    likedAt: string;
  }
  
  interface Comment {
    userId: number;
    content: string;
    postedAt: string;
  }
  
  interface Post {
    id: number;
    content: string;
    likes: Like[]; 
    comments: Comment[];
  }
  
  interface PostFooterProps {
    post: Post;
  }

const PostFooter = ({ post }: PostFooterProps) => {
    const [likes, setLikes] = useState(post.likes ? post.likes.length : 0);
    const [liked, setLiked] = useState(false);

    const handleLike = async () => {
      try {
          const response = await fetch(`/api/posts/${post.id}/like`, {
              method: "POST",
              headers: {
                  "Content-Type": "application/json",
              },
              body: JSON.stringify({ userId: "currentUserId" }),
          });

          if (response.ok) {
              const data = await response.json();
              setLikes(data.likes); 
              setLiked(data.liked); 
          } else {
              console.error("Failed to update like");
          }
      } catch (error) {
          console.error("Error liking post:", error);
      }
  };

    return (
      <div className="flex justify-between">
        <div className="flex items-center gap-2 text-gray-600 text-base">
                <button onClick={handleLike}>
                    <Heart className={`w-6 h-6 ${liked ? "text-red-500" : ""}`} />
                </button>
                <span>{likes}</span>
          </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><MessageCircle className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><Share2 className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><ShoppingBag className="w-6 h-6" /></button>
            <span>{post.comments ? post.comments.length  : 0}</span>
        </div>
      </div>
    );
  };

  export default PostFooter