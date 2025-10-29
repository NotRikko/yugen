import { fetchClient } from "@/shared/api/fetchClient";
import type { PostDTO } from "../types/postTypes";

export const postApi = {
  createPost: async (data: {
    artistId: number | null;
    content: string;
    productId?: number | null;
    files?: File[];
  }): Promise<PostDTO> => {
    const formData = new FormData();

    const postData = {
      artistId: data.artistId,
      content: data.content,
      productId: data.productId ?? null,
    };
    formData.append(
      "post",
      new Blob([JSON.stringify(postData)], { type: "application/json" })
    );

    if (data.files && data.files.length > 0) {
      data.files.forEach((file) => formData.append("files", file));
    }

    const result = await fetchClient<PostDTO>("/posts/create", {
      method: "POST",
      body: formData,
      auth: true, 
    });

    if (!result) throw new Error("Failed to create post");

    return result;
  },

  updatePost: async (postId: number, updatedContent: string) => {
    return fetchClient<PostDTO>(`/posts/${postId}`, {
      method: "PUT",
      body: JSON.stringify({ content: updatedContent }),
      auth: true,
    });
  },

  likePost: (postId: number, userId: number) =>
    fetchClient<{ likes: number; liked: boolean }>(`/posts/${postId}/like`, {
      method: "POST",
      body: JSON.stringify({ userId }),
      auth: true,
  }),

  getPostsByArtistName: (artistName: string) =>
    fetchClient<PostDTO[]>(`/artists/${artistName}/posts`, { method: "GET" }),
  
  getPostsByArtistId: (artistId: number) =>
    fetchClient<PostDTO[]>(`/artists/${artistId}/posts`, { method: "GET" }),


  deletePost: (postId: number) =>
    fetchClient<void>(`/posts/${postId}` , {
      method: "DELETE",
      auth: true,
    })
};