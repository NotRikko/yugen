import { useState, useEffect, useRef } from "react";
import Post from "@/features/posts/components/Post";
import PostCreate from "@/features/posts/components/PostCreate";
import TrendingArtistsBar from "@/features/feed/components/TrendingArtistsBar";
import { useFeed } from "../hooks/useFeed";
import { usePost } from "../../posts/hooks/usePost";
import { useArtist } from "@/features/artists/hooks/useArtist";

import PostModalWrapper from "@/features/posts/components/PostModalWrapper";

function FeedMain(): JSX.Element {
  const [userFeed, setUserFeed] = useState(false);

  const { posts, loading, loadMore } = useFeed({ userFeed, size: 10 });
  const { createPostOptimistic, deletePostOptimistic, updatePostOptimistic} = usePost();

  const [selectedPostId, setSelectedPostId] = useState<number | null>(null);
  
  const { trendingArtists, loadingTrendingArtists } = useArtist();

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
    <div className="grid grid-cols-[2fr_1fr] h-screen">
      <div className="flex flex-col items-center px-4 xl:px-12 my-12 w-full overflow-y-auto">
        <div className="flex gap-4 mb-4">
          <button
            className={`px-4 py-2 rounded ${!userFeed ? "bg-blue-500 text-white" : "bg-gray-200"}`}
            onClick={() => setUserFeed(false)}
          >
            Global Feed
          </button>
          <button
            className={`px-4 py-2 rounded ${userFeed ? "bg-blue-500 text-white" : "bg-gray-200"}`}
            onClick={() => setUserFeed(true)}
          >
            My Feed
          </button>
        </div>

        <div className="flex flex-col gap-4 w-full">
          <PostCreate onSubmit={createPostOptimistic} />

          {posts.map((post) => (
            <Post 
              key={post.id} 
              post={post} 
              onSelect={() => setSelectedPostId(post.id!)}
              onUpdate={() => setSelectedPostId(post.id!)}
              onDelete={() => deletePostOptimistic(post.id!)} />
          ))}

          {loading && <div className="text-center py-8">Loading posts...</div>}
          {!loading && posts.length === 0 && (
            <div className="text-center py-8">No posts found.</div>
          )}

          <div ref={loadMoreRef} />
        </div>
      </div>

      <TrendingArtistsBar
        trendingArtists={trendingArtists}
        loadingTrendingArtists={loadingTrendingArtists}
      />

      {selectedPostId && (
        <PostModalWrapper
          postId={selectedPostId}
          onClose={() => setSelectedPostId(null)}
          updatePost={updatePostOptimistic}
        />
      )}
    </div>
  );
}

export default FeedMain;