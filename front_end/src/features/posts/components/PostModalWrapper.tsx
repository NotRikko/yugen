import { useEffect, useState } from "react";
import { postApi } from "../api/postApi";
import PostModal from "./PostModal";
import type { PostDTO } from "../types";

interface PostModalWrapperProps {
  postId: number;
  onClose: () => void;
  updatePost: (postId: number, updatedContent: string) => Promise<void>;
  deletePost: (postId: number) => Promise<void>;
}

const PostModalWrapper: React.FC<PostModalWrapperProps> = ({
  postId,
  onClose,
  updatePost,
}) => {
  const [post, setPost] = useState<PostDTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPost = async () => {
      setLoading(true);
      try {
        const result = await postApi.getPostDetails(postId);
        setPost(result ?? null);
      } catch (error) {
        console.error("Failed to fetch post:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPost();
  }, [postId]);

  const handleBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) onClose();
  };

  if (loading) return <div>Loading post...</div>;
  if (!post) return null;

  return (
    <div
      className="fixed inset-0 bg-opacity-50 flex justify-center items-center z-50"
      onClick={handleBackgroundClick}
    >
      <div
        className="bg-white rounded-xl shadow-lg w-full md:w-4/5 lg:w-3/4 relative max-h-[95vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()} 
      >
        <PostModal post={post} updatePost={updatePost} />
      </div>
    </div>
  );
};

export default PostModalWrapper;