import PostFooter from "./PostFooter";
import CommentSection from "@/features/comments/components/CommentSection";
import CommentCreate from "@/features/comments/components/CommentCreate";
import { FollowButton } from "@/features/follow/components/FollowButton";
import type { PostDTO } from "../types/postTypes";
import { useNavigate } from "react-router-dom";
import { useComment } from "@/features/comments/hooks/useComment";
import { useEffect } from "react";

interface PostDetailsProps {
  post: PostDTO;
}

export default function PostModal({ post }: PostDetailsProps) {
  const navigate = useNavigate();
  const { createComment, deleteComment, comments, setComments } = useComment();

  useEffect(() => {
    setComments(post.comments);
  }, [post.comments, setComments]);

  const handleCommentSubmit = async (content: string) => {
    await createComment(post.id, content);
  };

  const handleCommentDelete = async (commentId: number) => {
      await deleteComment(commentId);
  };

  const handleArtistClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigate(`/artist/${post.artist.artistName}`);
  };

  return (
    <div className="w-5/6 h-5/6 mx-auto my-6 p-8 border rounded-2xl shadow-xl bg-white overflow-y-auto">
      <div className="flex items-center gap-4 mb-6">
        <img
          onClick={handleArtistClick}
          src={
            post.artist.profilePictureUrl ||
            "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg"
          }
          onError={(e) => {
            e.currentTarget.src =
              "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
          }}
          className="cursor-pointer w-14 h-14 rounded-full object-cover"
          alt="Post image"
        />
        <p
          onClick={handleArtistClick}
          className="cursor-pointer text-blue-600 text-lg font-semibold"
        >
          {post.artist.artistName}
        </p>

        <FollowButton artistId={post.artist.id} />
      </div>

      <p className="text-gray-800 text-base mb-6">{post.content}</p>

      {post.images?.length > 0 && (
        <div
          className={`
            grid gap-4 mb-8
            ${post.images.length === 1 ? "grid-cols-1" : ""}
            ${post.images.length === 2 ? "grid-cols-2" : ""}
            ${post.images.length >= 3 ? "grid-cols-2 grid-rows-2" : ""}
          `}
        >
          {post.images.map((image, index) => (
            <img
              key={index}
              src={image.url}
              className={`
                w-full rounded-xl object-cover
                ${post.images.length >= 3 ? "max-h-[300px]" : "max-h-[600px]"}
                min-h-[300px]
              `}
              alt={`Post image ${index + 1}`}
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
