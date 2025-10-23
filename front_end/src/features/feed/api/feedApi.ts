import { fetchClient } from "@/shared/api/fetchClient";
import { Post } from "@/features/posts/types/postTypes";

export const feedApi = {
    getGlobalFeed: (page = 0, size = 20) => 
      fetchClient<Post[]>(`/feed/global?page=${page}&size=${size}`, { method: "GET" }),
  
    getUserFeed: (page = 0, size = 20) =>
      fetchClient<Post[]>(`/feed/user?page=${page}&size=${size}`, { method: "GET" }),
  };