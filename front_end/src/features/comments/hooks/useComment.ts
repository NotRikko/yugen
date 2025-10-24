import { useState } from "react";
import { commentApi } from "../api/commentApi";
import type { PartialComment } from "../types/commentTypes";

export function useComment() {
  const [comments, setComments] = useState<PartialComment[]>([]);
  const [loading, setLoading ] = useState<boolean>(true);
  const deleteComment = async (commentId: number) => {
    setLoading(true);
    const prevComments = [...comments];
    try {
      await commentApi.deleteComment(commentId);
    } catch (err) {
      console.error("Failed to delete comment:", err);
      setComments(prevComments);
    } finally {
      setLoading(false);
    }
  };

  return { comments,  deleteComment, loading };

}