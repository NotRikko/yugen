import { fetchClient } from "@/shared/api/fetchClient";
import type { FeedResponseDTO } from "../types";

export const feedApi = {
    getGlobalFeed: (page = 0, size = 20) => 
      fetchClient<FeedResponseDTO>(`/feed/global?page=${page}&size=${size}`, { method: "GET" }),
  
    getUserFeed: (page = 0, size = 20) =>
      fetchClient<FeedResponseDTO>(`/feed/user?page=${page}&size=${size}`, { method: "GET" }),
  };