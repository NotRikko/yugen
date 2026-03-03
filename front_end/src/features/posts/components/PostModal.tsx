import PostFooter from "./PostFooter";
import CommentSection from "@/features/comments/components/CommentSection";
import CommentCreate from "@/features/comments/components/CommentCreate";
import { FollowButton } from "@/features/follow/components/FollowButton";
import type { PostDetailsDTO } from "../types";
import { useNavigate } from "react-router-dom";
import { useComment } from "@/features/comments/hooks/useComment";
import { useEffect, useState } from "react";
import { useUser } from "@/features/user/useUserContext";

interface PostModalProps {
  post: PostDetailsDTO;
  updatePost: (postId: number, content: string) => void;
}

const DEFAULT_IMAGE =
  "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";

const PostModal = ({ post, updatePost }: PostModalProps) => {
  const navigate = useNavigate();
  const { user } = useUser();
  const {
    comments,
    createComment,
    deleteComment,
    setComments,
  } = useComment(post.comments?.content ?? []);

  const [isEditing, setIsEditing] = useState(false);
  const [content, setContent] = useState(post.content ?? "");

  const images = post.imageUrls ?? [];

  useEffect(() => {
    if (post.comments?.content) setComments(post.comments.content);
  }, [post.comments, setComments]);

  const handleArtistClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (post.artist?.artistName) navigate(`/artist/${post.artist.artistName}`);
  };

  const handleSaveClick = () => {
    if (!post.id || content === post.content) return;
    updatePost(post.id, content);
    setIsEditing(false);
  };

  const handleCommentSubmit = async (commentContent: string) => {
    if (!post.id) return;
    await createComment(post.id, commentContent);
  };

  const handleCommentDelete = async (commentId: number) => {
    await deleteComment(commentId);
  };

  return (
    <div className="flex w-full h-full">
      <div className="w-1/2 bg-black flex items-center justify-center p-4">
        {images.length > 0 ? (
          <img
            src={images[0]}
            alt="Post"
            className="object-contain max-h-full max-w-full rounded-xl"
          />
        ) : (
          <img
            src={DEFAULT_IMAGE}
            alt="Default"
            className="object-cover w-full h-full rounded-xl"
          />
        )}
      </div>

      <div className="w-1/2 flex flex-col h-full overflow-y-auto border-l">
        <div className="flex items-center gap-4 p-4 border-b">
          <img
            src={post.artist?.profilePictureUrl ?? DEFAULT_IMAGE}
            onClick={handleArtistClick}
            onError={(e) => (e.currentTarget.src = DEFAULT_IMAGE)}
            className="cursor-pointer w-12 h-12 rounded-full object-cover"
            alt="Artist profile"
          />
          <div className="flex flex-col">
            <p
              onClick={handleArtistClick}
              className="cursor-pointer font-semibold text-gray-900"
            >
              {post.artist?.artistName ?? "Unknown Artist"}
            </p>
            {post.artist?.id && <FollowButton artistId={post.artist.id} />}
          </div>
        </div>

        <div className="p-4 border-b">
          {isEditing ? (
            <div className="flex flex-col gap-2">
              <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                className="border p-2 rounded w-full min-h-[80px]"
              />
              <div className="flex gap-2">
                <button
                  onClick={handleSaveClick}
                  className="bg-blue-500 text-white px-3 py-1 rounded"
                >
                  Save
                </button>
                <button
                  onClick={() => setIsEditing(false)}
                  className="bg-gray-200 px-3 py-1 rounded"
                >
                  Cancel
                </button>
              </div>
            </div>
          ) : (
            <>
              <p className="text-gray-800 whitespace-pre-wrap">{post.content}</p>
              {user?.id === post.artist?.id && !isEditing && (
                <button
                  onClick={() => setIsEditing(true)}
                  className="text-sm text-blue-500 mt-2"
                >
                  Edit
                </button>
              )}
            </>
          )}
        </div>

        <div className="p-4 border-b">
          <PostFooter post={post} />
        </div>

        <div className="flex-1 flex flex-col overflow-hidden">
          <div className="flex-1 overflow-y-auto p-4">
            <CommentSection comments={comments} onDelete={handleCommentDelete} />
          </div>
          <div className="border-t p-4">
            <CommentCreate onSubmit={handleCommentSubmit} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostModal;