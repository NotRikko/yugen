import { useState } from "react";

interface Comment {
    userId: number;
    content: string;
    postedAt: string;
}

interface Like {
    userId: number;
    likedAt: string;
}

interface Post {
    userId: number;
    content: string;
    image: string;
    artist: string;
    likes: Like[] | null;
    comments: Comment[] | null;
}

interface PostProps {
    post: Post;
}


function Post({ post }: PostProps) {
    return (
        <>
            <p>{post.content}</p>
            <img src={post.image} alt={post.artist} />
        </>
    );
}

export default Post
