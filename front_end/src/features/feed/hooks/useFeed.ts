import { useState, useEffect } from "react";
import { feedApi } from "../api/feedApi";
import type { Post } from "@/features/posts/types/postTypes";

export function useFeed({ userFeed = false, page = 0, size = 20 } = {}) {
    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchFeed = async () => {
            setLoading(true);
            try {
                const result = userFeed
                    ? await feedApi.getUserFeed(page, size)
                    : await feedApi.getGlobalFeed(page, size);
                setPosts(result || []);
            } catch (error) {
                console.error("Error fetching feed:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchFeed();
    }, [userFeed, page, size]);

    return { posts, loading };
}
