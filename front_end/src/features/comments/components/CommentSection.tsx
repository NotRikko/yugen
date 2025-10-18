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
                <div className="flex items-center gap-3 mb-1">
                  <img
                    src={c.user.image || "https://i1.sndcdn.com/artworks-oVGHBbuwyLdlUcdL-AjMmqA-t500x500.jpg"} 
                    alt={c.user.displayName}
                    className="w-8 h-8 rounded-full object-cover"
                  />
                  <p className="text-base font-semibold">{c.user.displayName}</p>
                </div>
  
                <p className="text-gray-700 text-base">{c.content}</p>
                <p className="text-sm text-gray-400">{c.postedAt}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    );
  }