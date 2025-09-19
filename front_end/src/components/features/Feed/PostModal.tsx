import Post from "./Post";
import PostFooter from "./PostFooter";


interface PostDetailsProps {
    post: Post;
  }
  function PostModal({ post }: PostDetailsProps) {
    return (
      <div className="w-5/6 h-5/6 mx-auto my-6 p-8 border rounded-2xl shadow-xl bg-white overflow-y-auto">
        <div className="flex items-center gap-4 mb-6">
          <img
            src={post.artist.image}
            className="w-14 h-14 rounded-full object-cover"
          />
          <p className="text-blue-600 text-lg font-semibold">
            {post.artist.artistName}
          </p>
        </div>
  
        <p className="text-gray-800 text-base mb-6">{post.content}</p>
  
        {post.images?.length > 0 && (
          <div
            className={`
              grid gap-4 mb-8
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
                  w-full rounded-xl object-cover
                  ${post.images.length >= 3 ? "max-h-[300px]" : "max-h-[600px]"}
                  min-h-[300px]
                `}
                alt={`Post image ${index + 1}`}
              />
            ))}
          </div>
        )}
  
        <PostFooter post={post} />
  
        <div className="mt-8">
          <h3 className="font-semibold text-lg text-gray-700 mb-4">Comments</h3>
          {post.comments.length === 0 ? (
            <p className="text-gray-500 text-base">No comments yet.</p>
          ) : (
            <ul className="space-y-5">
              {post.comments.map((c, i) => (
                <li key={i} className="border-b pb-3">
                  <p className="text-base font-semibold">User {c.userId}</p>
                  <p className="text-gray-700 text-base">{c.content}</p>
                  <p className="text-sm text-gray-400">{c.postedAt}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    );
  }
  

export default PostModal;