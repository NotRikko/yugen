import { useState, useEffect } from "react"
import Post from "../components/features/Feed/Post";
import PostCreate from "../components/features/Feed/PostCreate";
import FeedTrendingBar from "@/components/features/Feed/FeedTrendingBar";

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
                        <Post key={post.id} post={post} />
                    ))}
                </div>
            </div>
            <FeedTrendingBar />
        </div>
    )
}

export default FeedMain