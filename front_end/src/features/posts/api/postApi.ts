import { Post } from "../types/postTypes";

export const postApi = {
    createPost: async (data: {
        artistId: number | null;
        content: string;
        productId?: number | null;
        files?: File[];
      }): Promise<Post> => {
        const API_URL = import.meta.env.VITE_API_URL;
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
    
        const response = await fetch(`${API_URL}/posts/create`, {
          method: "POST",
          body: formData,
          credentials: "include",
        });
    
        if (!response.ok) {
          const errText = await response.text();
          throw new Error(errText || "Failed to create post");
        }
    
        return response.json() as Promise<Post>;
      },
}