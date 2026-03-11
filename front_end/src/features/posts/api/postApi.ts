import { fetchClient } from "@/shared/api/fetchClient";
import { type PostDetailsDTO, type PostDTO, type PostPageDTO } from "../types";

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

  getPostById: (id: number) => 
    fetchClient<PostDTO>(`/posts/${id}`, { method: "GET"}),

  getPostDetails: (id: number) =>
    fetchClient<PostDetailsDTO>(`/posts/${id}/details`, { method: "GET" }),

  
  getPostsByArtistId: async (artistId: number): Promise<PostDTO[]> => {
    const response = await fetchClient<PostPageDTO>(
      `/artists/${artistId}/posts`,
      { method: "GET" }
    );

    if (!response) throw new Error("Posts not found");
    return response.content ?? [];
  },

  deletePost: (postId: number) =>
    fetchClient<void>(`/posts/${postId}` , {
      method: "DELETE",
      auth: true,
    })
};