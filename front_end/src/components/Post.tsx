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

interface Artist {
    id: number;
    artistName: string;
    image: string;
    user: {
        id: number;
        username: string;
        displayName: string;
        email: string;
        image: string;
    };
}

interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    image: string;
    artist: Artist;
    series: any[]; 
    collections: any[];
    quantityInStock: number;
}

interface Post {
    id: number;
    content: string;
    artist: Artist;
    product: Product;
    likes: Like[];
    comments?: Comment[];
}

interface PostProps {
    post: Post;
}


function Post({ post }: PostProps) {
    return (
        <>
            <p>{post.content}</p>
            <p>By: {post.artist.artistName}</p>
            <img src={post.product.image} alt={post.product.name} />
        </>
    );
}
export default Post
