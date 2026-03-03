import { Heart, MessageCircle, ShoppingBag, Share2 } from "lucide-react";
import { useState } from "react";
import { useUser } from "@/features/user/useUserContext";
import { postApi } from "../api/postApi";
import ProductModalWrapper from "@/features/products/components/ProductModalWrapper";
import type { PostDetailsDTO } from "../types";

interface PostFooterProps {
  post: PostDetailsDTO;
}

const PostFooter = ({ post }: PostFooterProps) => {
  const { user } = useUser();

  const [likes, setLikes] = useState(post.likeCount ?? 0);
  const [liked, setLiked] = useState(!!post.likedByCurrentUser);
  const [showProductModal, setShowProductModal] = useState(false);

  const handleLike = async () => {
    if (!user?.id || !post.id) return;

    setLiked(!liked);
    setLikes((prev) => prev + (liked ? -1 : 1));

    try {
      const data = await postApi.likePost(post.id, user.id);
      if (data) {
        setLikes(data.likes);
        setLiked(data.liked);
      }
    } catch (error) {
      console.error("Error liking post:", error);
      setLiked(liked);
      setLikes((prev) => prev + (liked ? 1 : -1));
    }
  };

  const handleViewProduct = () => {
    if (!post.productId) return;
    setShowProductModal(true);
  };

  return (
    <div className="flex justify-between relative">
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
        <span>{post.comments?.totalElements ?? 0}</span>
      </div>

      <div className="flex items-center gap-2 text-gray-600 text-base">
        <button>
          <Share2 className="w-6 h-6" />
        </button>
      </div>

      {post.productId && (
        <div className="flex items-center gap-2 text-gray-600 text-base">
          <button onClick={handleViewProduct}>
            <ShoppingBag className="w-6 h-6" />
          </button>
        </div>
      )}

      {showProductModal && post.productId && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
          onClick={(e) => {
            if (e.target === e.currentTarget) setShowProductModal(false);
          }}
        >
          <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
            <ProductModalWrapper 
              productId={post.productId} 
              onClose={() => setShowProductModal(false)}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default PostFooter;