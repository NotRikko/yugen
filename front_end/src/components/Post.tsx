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
        <div className="w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white max-h-[600px]">
            <div className="flex items-center gap-2">
                <img 
                    src={post.artist.image}
                    className="w-10 h-10 rounded-full object-cover"
                />
                <p className="text-blue-500 text-sm font-semibold">{post.artist.artistName}</p>
             </div>
            <p className="text-gray-800 text-sm my-2">{post.content}</p>
            {post.product.image && (
                <img
                    src={post.product.image}
                    alt={post.product.name}
                    className="w-full max-h-[400px] object-cover mt-2 mb-12 rounded-lg"
                />
            )}
        </div>
    );
}
export default Post
