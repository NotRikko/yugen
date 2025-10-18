import { fetchClient } from "@/shared/api/fetchClient";
import { PartialComment } from "../types/commentTypes";

export const commentApi = {
  createPostComment: async (data: {
    userId: number | null;
    postId: number;
    content: string;
  }): Promise<PartialComment> => {

    const result = await fetchClient<PartialComment>("/comments/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
      auth: true,
    });

    if (!result) throw new Error("Failed to create comment");

    return result;
  },

  likeComment: (commentId: number, userId: number) =>
    fetchClient<{ likes: number; liked: boolean }>(
      `/comments/${commentId}/like`,
      {
        method: "POST",
        body: JSON.stringify({ userId }),
        auth: true,
      }
    ),

  deleteComment: (commentId: number) =>
    fetchClient<void>(`/comments/${commentId}`, {
      method: "DELETE",
      auth: true,
    }),
};