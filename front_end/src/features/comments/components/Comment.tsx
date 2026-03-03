import type { CommentDTO } from "../types";
import { ReusableDropdown } from "@/shared/ReusableDropdown";

interface CommentItemProps {
  comment: CommentDTO;
  currentUserId?: number;
  onDelete?: (commentId: number) => void;
}

export default function CommentItem({ comment, currentUserId, onDelete }: CommentItemProps) {
  const isAuthor = currentUserId === comment.user!.id;

  const handleDelete = () => {
    if (onDelete) onDelete(comment.id!);
  };

  return (
    <li className="border-b pb-3">
      <div className="flex items-center justify-between mb-1">
        <div className="flex items-center gap-3">
          <img
            src={comment.user!.avatarUrl}
            alt={comment.user!.displayName}
            className="w-8 h-8 rounded-full object-cover"
          />
          <p className="text-base font-semibold">{comment.user!.displayName}</p>
        </div>

        {isAuthor && (
          <ReusableDropdown
            triggerText="•••"
            items={[
              { label: "Delete", onClick: handleDelete },
            ]}
          />
        )}
      </div>

      <p className="text-gray-700 text-base">{comment.content}</p>
      <p className="text-sm text-gray-400">{comment.createdAt}</p>
    </li>
  );
}