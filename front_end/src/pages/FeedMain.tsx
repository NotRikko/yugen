import { useState, useEffect } from "react"
import Post from "../components/Post"
import PostThumb from "@/components/PostThumb";
import FeedSidebar from "@/components/FeedSidebar";
import FeedTrendingBar from "@/components/FeedTrendingBar";

function FeedMain(): JSX.Element {
    const [posts, setPosts] = useState<Post[]>([]);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const postsResponse = await fetch("http://localhost:8080/posts/all", { mode: "cors"});

                if (!postsResponse.ok) {
                    throw new Error("Issue with network response")
                }

                const postsData = await postsResponse.json()
                setPosts(postsData);
            } catch (error) {
                console.error("Error fetching posts", error)
            }
         
        };
        fetchPosts();
    }, [])
    
    return(
        <div className="grid grid-cols-[20%_1fr_20%]">
            <FeedSidebar />
            <div className="flex flex-col justify-center items-center mx-24 my-12">
                <div className="grid grid-cols-3 gap-4">
                    {posts.map((post) => (
                        <PostThumb key={post.id} post={post} />
                    ))}
                </div>
                <div className="flex flex-col w-full">
                {posts.map((post) => (
                    <Post key={post.id} post={post} />
                ))}
                </div>
            </div>
            <FeedTrendingBar />
         </div>
    )
}

export default FeedMain