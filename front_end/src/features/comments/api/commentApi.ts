import { fetchClient } from "@/shared/api/fetchClient";
import { PartialComment } from "../types/commentTypes";

export const commentApi = {
  createComment: async (data: {
    userId: number | null;
    postId: number;
    content: string;
    files?: File[];
  }): Promise<PartialComment> => {
    const formData = new FormData();

    const commentData = {
      userId: data.userId,
      postId: data.postId,
      content: data.content,
    };
    formData.append(
      "post",
      new Blob([JSON.stringify(commentData)], { type: "application/json" })
    );

    if (data.files && data.files.length > 0) {
      data.files.forEach((file) => formData.append("files", file));
    }

    const result = await fetchClient<PartialComment>("/comments/create", {
      method: "POST",
      body: formData,
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