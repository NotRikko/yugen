import { useState, useEffect } from "react";
import { feedApi } from "../api/feedApi";
import type { Post } from "@/features/posts/types/postTypes";

export function useFeed() {
    const [posts, setPosts] = useState<Post[]>([]); 
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchFeed = async () => {
            setLoading(true);
            try {
                const result = await feedApi.getFeedPosts();
                setPosts(result || []);
                } catch (error) {
                console.error("Error fetching feed:" , error);
            } finally {
                setLoading(false);
            }
        };

        fetchFeed();
    }, []);

    return { posts, loading };
}

