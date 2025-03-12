import PostFooter
 from "./PostFooter";
interface Comment {
    userId: number;
    content: string;
    postedAt: string;
}

interface Like {
    userId: number;
    likedAt: string;
}

interface Images {
    imageId: number;
    url: string;
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

interface Series {
    id: number;
}

interface Collection {
    id: number;
}

interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    image: string;
    artist: Artist;
    series: Series[]; 
    collections: Collection[];
    quantityInStock: number;
}

interface Post {
    id: number;
    content: string;
    artist: Artist;
    product: Product;
    likes: Like[];
    images: Images[];
    comments: Comment[];
}

interface PostProps {
    post: Post;
}


function Post({ post }: PostProps) {
    return (
        <div className="w-5/6 mx-auto p-8 border rounded-lg shadow-md bg-white max-h-[800px]">
            <div className="flex items-center gap-2">
                <img 
                    src={post.artist.image}
                    className="w-10 h-10 rounded-full object-cover"
                />
                <p className="text-blue-500 text-sm font-semibold">{post.artist.artistName}</p>
             </div>
            <p className="text-gray-800 text-sm my-2">{post.content}</p>
            {post.images && post.images.length > 0 && (
                <div
                    className={`
                        grid gap-4 my-4 
                        ${post.images.length === 1 ? "grid-cols-1" : ""} 
                        ${post.images.length === 2 ? "grid-cols-2" : ""} 
                        ${post.images.length >= 3 ? "grid-cols-2 grid-rows-2" : ""}
                    `}
                >
                    {post.images.map((image, index) => (
                        <img
                            key={index}
                            src={image.url}
                            className={`
                                w-full 
                                ${post.images.length >= 3 ? "max-h-[250px]" : "max-h-[500px]"} 
                                min-h-[250px] object-cover rounded-lg
                            `}
                            alt={`Post image ${index + 1}`}
                        />
                    ))}
                </div>
            )}
            <PostFooter post={post} />
        </div>
    );
}
export default Post
