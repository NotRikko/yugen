import { useState } from "react";
import type { PostDTO } from "@/features/posts/types/postTypes";
import { postApi } from "@/features/posts/api/postApi";
import { useUser } from "@/features/user/useUserContext";

export function usePost(initialPosts: PostDTO[] = []) {
  const { user } = useUser();
  const [posts, setPosts] = useState<PostDTO[]>(initialPosts);
  const [loading, setLoading] = useState(false);

  const createPostOptimistic = async (newPostData: { content: string; files?: File[] }) => {
    if (!user || user.isGuest) return;

    const tempPost: PostDTO = {
      id: Date.now(),
      content: newPostData.content,
      images: [],
      artist: {
        id: user.artistId ?? 0,
        artistName: user.displayName,
        profilePictureUrl: user.image,
        user: {
          id: user.id,
          username: user.username,
          displayName: user.displayName,
          email: user.email,
          image: user.image,
        },
      },
      likes: [],
      comments: [],
    };

    setPosts((prev) => [tempPost, ...prev]);

    try {
      const createdPost = await postApi.createPost({
        artistId: user.artistId!,
        content: newPostData.content,
        files: newPostData.files,
      });
      setPosts((prev) => prev.map((p) => (p.id === tempPost.id ? createdPost : p)));
    } catch (error) {
      setPosts((prev) => prev.filter((p) => p.id !== tempPost.id));
      console.error("Failed to create post:", error);
    }
  };

  const updatePostOptimistic = async (postId: number, updatedContent: string) => {
    const postToUpdate = posts.find((p) => p.id === postId);
    if (!postToUpdate) return;
  
    const originalPost = { ...postToUpdate };
  
    setPosts((prev) =>
      prev.map((p) => (p.id === postId ? { ...p, content: updatedContent } : p))
    );
  
    try {
      const updatedPost = await postApi.updatePost(postId, updatedContent);
  
      setPosts((prev) => prev.map((p) => (p.id === postId ? updatedPost : p)));
    } catch (error) {
      console.error("Failed to update post:", error);
  
      setPosts((prev) => prev.map((p) => (p.id === postId ? originalPost : p)));
    }
  };

  const deletePostOptimistic = async (postId: number) => {
    const postToDelete = posts.find((p) => p.id === postId);
    if (!postToDelete) return;

    setPosts((prev) => prev.filter((p) => p.id !== postId));

    try {
      await postApi.deletePost(postId);
    } catch (error) {
      console.error("Failed to delete post:", error);
      setPosts((prev) => [postToDelete, ...prev]);
    }
  };

  return { posts, setPosts, loading, setLoading, createPostOptimistic, updatePostOptimistic, deletePostOptimistic };
}