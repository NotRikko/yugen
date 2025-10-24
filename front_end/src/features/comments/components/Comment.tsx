import type { PartialComment } from "../types/commentTypes";

interface CommentItemProps {
  comment: PartialComment;
  currentUserId?: number;
  onDelete?: (commentId: number) => void;
}

export default function CommentItem({ comment, currentUserId, onDelete }: CommentItemProps) {
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
          <button
            onClick={() => onDelete?.(comment.id)}
            className="text-sm text-red-500 hover:text-red-700 transition"
          >
            Delete
          </button>
        )}
      </div>

      <p className="text-gray-700 text-base">{comment.content}</p>
      <p className="text-sm text-gray-400">{comment.postedAt}</p>
    </li>
  );
}