import type { PartialComment } from "../types/commentTypes";

interface CommentSectionProps {
    postId: number;
    comments: PartialComment[];
}
  
export default function CommentSection({ postId, comments }: CommentSectionProps) {
return (
    <div>
        <h3 className="font-semibold text-lg text-gray-700 mb-4">Comments</h3>
        {comments.length === 0 ? (
            <p className="text-gray-500 text-base">No comments yet.</p>
        ) : (
            <ul className="space-y-5">
            {comments.map((c, i) => (
                <li key={i} className="border-b pb-3">
                <p className="text-base font-semibold">User {c.userId}</p>
                <p className="text-gray-700 text-base">{c.content}</p>
                <p className="text-sm text-gray-400">{c.postedAt}</p>
                </li>
            ))}
            </ul>
        )}
    </div>
);
}