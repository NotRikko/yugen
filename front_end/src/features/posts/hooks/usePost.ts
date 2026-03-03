import { useState } from "react";
import { postApi } from "../api/postApi";
import { useUser } from "@/features/user/useUserContext";
import type { PostDTO } from "../types";

export function usePost(
  initialPosts: PostDTO[] = [],
  externalSetPosts?: (posts: PostDTO[]) => void
) {
  const { user } = useUser();
  const [posts, setPosts] = useState<PostDTO[]>(initialPosts);
  const [loading, setLoading] = useState(false);

  const updatePosts = (newPosts: PostDTO[]) => {
    setPosts(newPosts);
    if (externalSetPosts) externalSetPosts(newPosts);
  };

  const createPostOptimistic = async (newPostData: { content: string; files?: File[] }) => {
    if (!user || user.isGuest) return;

    const tempPost: PostDTO = {
      id: Date.now(),
      content: newPostData.content,
      imageUrls: [],
      artistId: user.artistId,
      likeCount: 0,
      commentCount: 0,
    };

    updatePosts([tempPost, ...posts]);

    try {
      const createdPost = await postApi.createPost({
        artistId: user.artistId!,
        content: newPostData.content,
        files: newPostData.files,
      });
      updatePosts(posts.map((p) => (p.id === tempPost.id ? createdPost : p)));
    } catch (error) {
      updatePosts(posts.filter((p) => p.id !== tempPost.id));
      console.error("Failed to create post:", error);
    }
  };

  const updatePostOptimistic = async (postId: number, updatedContent: string) => {
    const postToUpdate = posts.find((p) => p.id === postId);
    if (!postToUpdate) return;

    const originalPost = { ...postToUpdate };
    updatePosts(posts.map((p) => (p.id === postId ? { ...p, content: updatedContent } : p)));

    try {
      const updatedPost = await postApi.updatePost(postId, updatedContent);
      updatePosts(posts.map((p) => (p.id === postId ? updatedPost : p)));
    } catch (error) {
      console.error("Failed to update post:", error);
      updatePosts(posts.map((p) => (p.id === postId ? originalPost : p)));
    }
  };

  const deletePostOptimistic = async (postId: number) => {
    const postToDelete = posts.find((p) => p.id === postId);
    if (!postToDelete) return;

    updatePosts(posts.filter((p) => p.id !== postId));

    try {
      await postApi.deletePost(postId);
    } catch (error) {
      console.error("Failed to delete post:", error);
      updatePosts([postToDelete, ...posts]);
    }
  };

  return {
    posts,
    setPosts: updatePosts,
    loading,
    setLoading,
    createPostOptimistic,
    updatePostOptimistic,
    deletePostOptimistic,
  };
}