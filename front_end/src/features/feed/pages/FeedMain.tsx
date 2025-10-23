import { useState, useEffect, useRef, useCallback } from "react";
import Post from "@/features/posts/components/Post";
import PostCreate from "@/features/posts/components/PostCreate";
import PostModal from "@/features/posts/components/PostModal";
import TrendingArtistsBar from "@/features/feed/components/TrendingArtistsBar";
import { useFeed } from "../hooks/useFeed";
import { useArtist } from "@/features/artists/hooks/useArtist";
import type { PostDTO } from "@/features/posts/types/postTypes";

function FeedMain(): JSX.Element {
  const [userFeed, setUserFeed] = useState(false);
  const [page, setPage] = useState(0);
  const [size] = useState(10); 

  const { posts: newPosts, loading } = useFeed({ userFeed, page, size });

  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [selectedPost, setSelectedPost] = useState<PostDTO | null>(null);

  const { trendingArtists, loadingTrendingArtists } = useArtist();

  const observer = useRef<IntersectionObserver | null>(null);
  const loadMoreRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (page === 0) {
      setPosts(newPosts);
    } else {
      setPosts((prev) => [...prev, ...newPosts]);
    }
  }, [newPosts, page]);

  const handleObserver = useCallback(
    (entries: IntersectionObserverEntry[]) => {
      const target = entries[0];
      if (target.isIntersecting && !loading) {
        setPage((prev) => prev + 1);
      }
    },
    [loading]
  );

  useEffect(() => {
    if (observer.current) observer.current.disconnect();

    observer.current = new IntersectionObserver(handleObserver, { threshold: 1.0 });
    if (loadMoreRef.current) observer.current.observe(loadMoreRef.current);

    return () => observer.current?.disconnect();
  }, [handleObserver]);

  const handleModalBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      setSelectedPost(null);
    }
  };

  return (
    <div className="grid grid-cols-[2fr_1fr] h-screen">
      <div className="flex flex-col items-center px-4 xl:px-12 my-12 w-full overflow-y-auto">
        <div className="flex gap-4 mb-4">
          <button
            className={`px-4 py-2 rounded ${!userFeed ? "bg-blue-500 text-white" : "bg-gray-200"}`}
            onClick={() => { setUserFeed(false); setPage(0); }}
          >
            Global Feed
          </button>
          <button
            className={`px-4 py-2 rounded ${userFeed ? "bg-blue-500 text-white" : "bg-gray-200"}`}
            onClick={() => { setUserFeed(true); setPage(0); }}
          >
            My Feed
          </button>
        </div>

        <div className="flex flex-col gap-4 w-full">
          <PostCreate />
          {posts.map((post) => (
            <Post key={post.id} post={post} onSelect={() => setSelectedPost(post)} />
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

      {selectedPost && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
          onClick={handleModalBackgroundClick}
        >
          <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
            <PostModal post={selectedPost} />
          </div>
        </div>
      )}
    </div>
  );
}

export default FeedMain;