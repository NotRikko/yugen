import type { PostDTO } from "../types";

interface PostThumbProps {
  post: PostDTO;
}

function PostThumb({ post }: PostThumbProps) {
  const firstImage = post.imageUrls?.[0] ?? "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg"; 

  return (
    <div className="p-4 border">
      <img
        src={firstImage}
        alt={post.content || "Post image"}
        className="w-full h-32 object-cover rounded-md"
        onError={(e) => {
          e.currentTarget.src =
            "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg";
        }}
      />
    </div>
  );
}

export default PostThumb;