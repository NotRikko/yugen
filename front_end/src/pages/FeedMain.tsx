import { useState, useEffect } from "react"
import Navbar from "../components/Navbar"
import Post from "../components/Post"

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
        <>
            <Navbar/>
            {posts.map((post) => (
                <Post key={post.id} post={post} />
            ))}
        </>
    )
}

export default FeedMain