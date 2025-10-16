import { fetchClient } from "@/shared/api/fetchClient";
import { Post } from "@/features/posts/types/postTypes";

export const feedApi = {
    getFeedPosts: () => 
        fetchClient<Post[]>(`/posts/`, { method: "GET" }),
}