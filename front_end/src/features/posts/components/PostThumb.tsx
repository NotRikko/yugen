import type { PostDTO } from "@/features/posts/types/postTypes";

interface PostThumbProps {
    post: PostDTO;
}


function PostThumb({ post }: PostThumbProps) {
    return (
        <div className="p-4 border">
            <img src={post.images[0].url} alt={post.content} />
        </div>
    );
}
export default PostThumb