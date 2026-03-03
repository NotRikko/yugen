import PostFooter from "./PostFooter";
import CommentSection from "@/features/comments/components/CommentSection";
import CommentCreate from "@/features/comments/components/CommentCreate";
import { FollowButton } from "@/features/follow/components/FollowButton";
import type { PostDetailsDTO } from "../types";
import { useNavigate } from "react-router-dom";
import { useComment } from "@/features/comments/hooks/useComment";
import { useEffect, useState } from "react";

interface PostDetailsProps {
  post: PostDetailsDTO;
  updatePost: (postId: number, content: string) => void

}

export default function PostModal({ post, updatePost }: PostDetailsProps) {
  const navigate = useNavigate();
  const { createComment, deleteComment, comments, setComments } = useComment();

  const [isEditing, setIsEditing] = useState(false);
  const [content, setContent] = useState(post.content || "");

  useEffect(() => {
    if (post.comments?.content) {
      setComments(post.comments.content);
    }
  }, [post.comments, setComments]);

  const handleEditClick = () => setIsEditing(true);

  const handleSaveClick = () => {
    if (post.id != null && content !== post.content) {
      updatePost(post.id, content);
    }
    setIsEditing(false);
  };

  const handleCommentSubmit = async (commentContent: string) => {
    if (post.id != null) {
      await createComment(post.id, commentContent);
    }
  };

  const handleCommentDelete = async (commentId: number) => {
    await deleteComment(commentId);
  };

  const handleArtistClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (post.artist?.artistName) {
      navigate(`/artist/${post.artist.artistName}`);
    }
  };

  const DEFAULT_IMAGE =
    "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";

  const images = post.imageUrls || [];


  return (
    <div className="w-5/6 h-5/6 mx-auto my-6 p-8 border rounded-2xl shadow-xl bg-white overflow-y-auto">
      <div className="flex items-center gap-4 mb-6">
        <img
          onClick={handleArtistClick}
          src={post.artist?.profilePictureUrl || DEFAULT_IMAGE}
          onError={(e) => (e.currentTarget.src = DEFAULT_IMAGE)}
          className="cursor-pointer w-14 h-14 rounded-full object-cover"
          alt="Artist profile"
        />

        <p
          onClick={handleArtistClick}
          className="cursor-pointer text-blue-600 text-lg font-semibold"
        >
          {post.artist?.artistName || "Unknown Artist"}
        </p>

        {post.artist?.id != null && <FollowButton artistId={post.artist.id} />}
      </div>

      {!isEditing && (
        <button onClick={handleEditClick} className="text-blue-500 mb-2">
          Edit
        </button>
      )}

      {isEditing ? (
        <div className="flex flex-col gap-2 mb-6">
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="border p-2 rounded w-full min-h-[100px]"
          />
          <div className="flex gap-2">
            <button
              onClick={handleSaveClick}
              className="bg-blue-500 text-white px-4 py-2 rounded"
            >
              Save
            </button>
            <button
              onClick={() => setIsEditing(false)}
              className="bg-gray-200 px-4 py-2 rounded"
            >
              Cancel
            </button>
          </div>
        </div>
      ) : (
        <p className="text-gray-800 text-base mb-6">{post.content}</p>
      )}

      {images.length > 0 && (
        <div
          className={`
            grid gap-4 mb-8
            ${images.length === 1 ? "grid-cols-1" : ""}
            ${images.length === 2 ? "grid-cols-2" : ""}
            ${images.length >= 3 ? "grid-cols-2 grid-rows-2" : ""}
          `}
        >
          {images.map((url, idx) => (
            <img
              key={idx}
              src={url}
              alt={`Post image ${idx + 1}`}
              className={`w-full rounded-xl object-cover min-h-[300px] ${
                images.length >= 3 ? "max-h-[300px]" : "max-h-[600px]"
              }`}
            />
          ))}
        </div>
      )}

      <PostFooter post={post} />

      <div className="mt-8">
        <CommentCreate onSubmit={handleCommentSubmit} />
        <CommentSection comments={comments} onDelete={handleCommentDelete} />
      </div>
    </div>
  );
}