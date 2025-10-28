import type { PostDTO } from "@/features/posts/types/postTypes";
import PostModal from "@/features/posts/components/PostModal";

interface PostModalWrapperProps {
  post: PostDTO | null;
  onClose: () => void;
}

const PostModalWrapper: React.FC<PostModalWrapperProps> = ({ post, onClose }) => {
  if (!post) return null;

  const handleBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) onClose();
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
      onClick={handleBackgroundClick}
    >
      <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
        <PostModal post={post} />
      </div>
    </div>
  );
};

export default PostModalWrapper;