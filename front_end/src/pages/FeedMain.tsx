import { useState, useEffect } from "react"
import Post from "../components/features/Feed/Post";
import FeedSidebar from "@/components/features/Feed/FeedSidebar";
import FeedTrendingBar from "@/components/features/Feed/FeedTrendingBar";
import { useUser } from "@/UserProvider";

function FeedMain(): JSX.Element {
    const [posts, setPosts] = useState<Post[]>([]);
    const {user} = useUser();

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
        <div className="grid sm:grid-cols-[1fr_3fr] xl:grid-cols-[25%_1fr_25%]">
            <FeedSidebar user={user} />
            <div className="flex flex-col justify-center items-center mx-4 xl:mx-12 my-12">
                <div className="flex flex-col gap-4 w-full">
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