import { useState } from "react";
import { commentApi } from "../api/commentApi";
import type { CommentDTO } from "../types";

export function useComment(initialComments: CommentDTO[] = []) {
  const [comments, setComments] = useState<CommentDTO[]>(initialComments);
  const [loading, setLoading] = useState<boolean>(false);

  const deleteComment = async (commentId: number) => {
    setLoading(true);
    const prevComments = [...comments];
    try {
      await commentApi.deleteComment(commentId);
      setComments(prevComments.filter(c => c.id !== commentId));
    } catch (err) {
      console.error("Failed to delete comment:", err);
      setComments(prevComments);
    } finally {
      setLoading(false);
    }
  };

  const createComment = async (postId: number, content: string) => {
    setLoading(true);
    try {
      const created = await commentApi.createPostComment({
        postId,
        content,
      });
      setComments(prev => [...prev, created]);
      return created;
    } catch (err) {
      console.error("Failed to create comment:", err);
      return null;
    } finally {
      setLoading(false);
    }
  };

  return { comments, createComment, deleteComment, loading, setComments };
}