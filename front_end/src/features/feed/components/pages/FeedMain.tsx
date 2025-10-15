import { useState, useEffect } from "react";
import Post from "@/features/posts/components/Post";
import PostCreate from "@/features/posts/components/PostCreate";
import PostModal from "@/features/posts/components/PostModal";
import FeedTrendingBar from "@/features/feed/components/FeedTrendingBar";

function FeedMain(): JSX.Element {
    const [posts, setPosts] = useState<Post[]>([]);
    const [selectedPost, setSelectedPost] = useState<Post | null>(null);

    useEffect(() => {
        const fetchPosts = async () => {
            const API_URL = import.meta.env.VITE_API_URL;
            try {
                const postsResponse = await fetch(`${API_URL}/posts/all`, { mode: "cors"});

                if (!postsResponse.ok) {
                    throw new Error("Issue with network response")
                }

                const postsData = await postsResponse.json()
                console.log(postsData);
                setPosts(postsData);
            } catch (error) {
                console.error("Error fetching posts", error)
            }
         
        };
        fetchPosts();
    }, [])
    
    return(
        <div className="grid grid-cols-[2fr_1fr]">
            <div className="flex flex-col justify-center items-center mx-4 xl:mx-12 my-12">
                <div className="flex flex-col gap-4 w-full">
                    <PostCreate/>
                    {posts.map((post) => (
                        <Post key={post.id} post={post} onSelect={() => setSelectedPost(post)} />
                    ))}
                </div>
            </div>
            <FeedTrendingBar />
            {selectedPost && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
                    <button
                    className="absolute top-3 right-3 text-gray-500 hover:text-gray-800"
                    onClick={() => setSelectedPost(null)}
                    >
                    ✕
                    </button>
                    <PostModal post={selectedPost} />
                </div>
                </div>
            )}
        </div>
        
    )
}

export default FeedMain