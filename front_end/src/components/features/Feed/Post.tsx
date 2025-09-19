import PostFooter from "./PostFooter";

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
    onSelect? : () => void;
}


function Post({ post, onSelect }: PostProps) {
    return (
      <div className="w-5/6 mx-auto p-8 border rounded-lg shadow-md bg-white max-h-[800px]">
        <div onClick={onSelect} className="cursor-pointer">
          <div className="flex items-center gap-2">
          <img
                src={post.artist.image || "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg"}
                onError={(e) => {
                    e.currentTarget.src = "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
                }}
                className="w-10 h-10 rounded-full object-cover"
            />
            <p className="text-blue-500 text-sm font-semibold">{post.artist.artistName}</p>
          </div>
          <p className="text-gray-800 text-sm my-2">{post.content}</p>
        </div>
  
        {post.images?.length > 0 && (
          <div className="grid gap-4 my-4">
            {post.images.map((image, index) => (
              <img key={index} src={image.url} className="..." alt={`Post image ${index + 1}`} />
            ))}
          </div>
        )}
  
        <PostFooter post={post} />
      </div>
    );
  }
export default Post
