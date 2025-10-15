interface PostThumb {
    id: number;
    product: Product;

}

interface Product {
    id: number;
    name: string;
    image: string;
}

interface Post {
    id: number;
    product: Product;
}

interface PostThumbProps {
    post: Post;
}


function PostThumb({ post }: PostThumbProps) {
    return (
        <div className="p-4 border">
            <img src={post.product.image} alt={post.product.name} />
        </div>
    );
}
export default PostThumb