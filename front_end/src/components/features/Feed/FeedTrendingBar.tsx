import { useState, useEffect } from "react"
import Post from "./Post";

function FeedTrendingBar(): JSX.Element {
    const [posts, setPosts] = useState<Post[]>([]);
    const [trendingArtists, setTrendingArtists] = useState([]);
    const [trendingTags, setTrendingTags] = useState([]);

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
        <div className="hidden lg:flex flex-col w-full border-l-2 bg-sidebar">
            <h1 className="mx-auto my-4 text-lg">Trending</h1>
            <ul className="flex flex-col gap-y-4">
                {posts.map((post) => (
                    <Post key={post.id} post={post} />
                ))}
            </ul>
        </div>
    )
}

export default FeedTrendingBar