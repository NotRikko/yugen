import { Heart, MessageCircle, ShoppingBag, Share2 } from "lucide-react"

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
    return (
        <div className="flex justify-between">
        <div className="flex items-center gap-2 text-gray-600 text-base">
            <button><Heart className="w-6 h-6"/></button>
            <span>{post.likes ? post.likes.length : 0}</span>
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