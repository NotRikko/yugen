import type { PartialComment } from "../types/commentTypes";
import DeleteCommentButton from "./DeleteCommentButton";
interface CommentItemProps {
  comment: PartialComment;
  currentUserId?: number;
  onDelete?: (commentId: number) => void;
}

export default function CommentItem({ comment, currentUserId, }: CommentItemProps) {
  const canDelete = currentUserId === comment.user.id;

  return (
    <li className="border-b pb-3">
      <div className="flex items-center justify-between mb-1">
        <div className="flex items-center gap-3">
          <img
            src={comment.user.image}
            alt={comment.user.displayName}
            className="w-8 h-8 rounded-full object-cover"
          />
          <p className="text-base font-semibold">{comment.user.displayName}</p>
        </div>

        {canDelete && (
          <DeleteCommentButton commentId={comment.id} />
        )}
      </div>

      <p className="text-gray-700 text-base">{comment.content}</p>
      <p className="text-sm text-gray-400">{comment.postedAt}</p>
    </li>
  );
}