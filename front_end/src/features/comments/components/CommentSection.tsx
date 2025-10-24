import type { PartialComment } from "../types/commentTypes";
import { useUser } from "@/features/user/useUserContext";
import { useComment } from "../hooks/useComment";
import Comment from "./Comment";

interface CommentSectionProps {
  comments: PartialComment[];
  onDeleteComment?: (commentId: number) => void;
}

export default function CommentSection({ comments }: CommentSectionProps) {
  const { user } = useUser();
  const { deleteComment } = useComment();

  return (
    <div>
      <h3 className="font-semibold text-lg text-gray-700 mb-4">Comments</h3>

      {comments.length === 0 ? (
        <p className="text-gray-500 text-base">No comments yet.</p>
      ) : (
        <ul className="space-y-5">
          {comments.map((comment) => (
            <Comment
              key={comment.id}
              comment={comment}
              currentUserId={user?.id}
              onDelete={deleteComment}
            />
          ))}
        </ul>
      )}
    </div>
  );
}