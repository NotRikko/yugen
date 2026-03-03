import { useState, useEffect, useRef } from "react";
import { useFeed } from "../hooks/useFeed";
import { usePost } from "../../posts/hooks/usePost";
import PostModalWrapper from "@/features/posts/components/PostModalWrapper";
import FeedPost from "@/features/posts/components/FeedPost";

function FeedMain(): JSX.Element {
  const [userFeed] = useState(false);
  const [selectedPostId, setSelectedPostId] = useState<number | null>(null);

  const { posts, loading, loadMore } = useFeed({ userFeed, size: 10 });
  const { deletePostOptimistic, updatePostOptimistic } = usePost();

  const observer = useRef<IntersectionObserver | null>(null);
  const loadMoreRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (observer.current) observer.current.disconnect();

    observer.current = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !loading) loadMore();
      },
      { threshold: 1.0 }
    );

    if (loadMoreRef.current) observer.current.observe(loadMoreRef.current);

    return () => observer.current?.disconnect();
  }, [loadMore, loading]);

  return (
    <div className="grid grid-cols-[4fr_1fr] h-screen">

      <div className="overflow-y-auto px-6 py-10">

        <div className="columns-2 sm:columns-3 md:columns-4 xl:columns-5 gap-6 space-y-6">

          {posts.map((post) => (
            <div
              key={post.id}
              className="break-inside-avoid cursor-pointer"
              onClick={() => setSelectedPostId(post.id!)}
            >
              <FeedPost post={post} />
            </div>
          ))}

        </div>

        {loading && (
          <div className="text-center py-10 text-gray-500">
            Loading more posts...
          </div>
        )}

        <div ref={loadMoreRef} />
      </div>

      {selectedPostId && (
        <PostModalWrapper
          postId={selectedPostId}
          onClose={() => setSelectedPostId(null)}
          updatePost={updatePostOptimistic}
          deletePost={deletePostOptimistic}
        />
      )}
    </div>
  );
}

export default FeedMain;