import { Heart, MessageCircle, ShoppingBag, Share2 } from "lucide-react"
import { useState } from "react"
import { useUser } from "@/features/user/UserProvider";
import { useCart } from "@/features/cart/CartProvider";
import type { PostDTO } from "@/features/posts/types/postTypes";
import { postApi } from "../api/postApi";

interface PostFooterProps {
  post: PostDTO;
}

const PostFooter = ({ post }: PostFooterProps) => {
  const { user } = useUser();
  const { addToCart } = useCart();
  const [likes, setLikes] = useState(post.likes?.length ?? 0);
  const [liked, setLiked] = useState(
    post.likes?.some((like) => like.userId === user?.id) ?? false
  );

  const handleLike = async () => {
    if (!user) return;

    try {
      const data = await postApi.likePost(post.id, user.id);
      if (!data) {
        console.error("No data returned from likePost");
        return;
      }

      setLikes(data.likes);
      setLiked(data.liked);
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handlePurchase = async () => {
    if (!post.product) {
      console.warn("No product associated with this post");
      return;
    }

    try {
      await addToCart(post.product.id, 1);
    } catch (error) {
      console.error("Error purchasing product:", error);
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
        <button>
          <MessageCircle className="w-6 h-6" />
        </button>
        <span>{post.comments?.length ?? 0}</span>
      </div>

      <div className="flex items-center gap-2 text-gray-600 text-base">
        <button>
          <Share2 className="w-6 h-6" />
        </button>
      </div>

      {post.product && (
        <div className="flex items-center gap-2 text-gray-600 text-base">
          <button onClick={handlePurchase}>
            <ShoppingBag className="w-6 h-6" />
          </button>
        </div>
      )}
    </div>
  );
};

export default PostFooter;