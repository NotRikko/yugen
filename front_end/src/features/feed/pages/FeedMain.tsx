import { useState } from "react";
import Post from "@/features/posts/components/Post";
import PostCreate from "@/features/posts/components/PostCreate";
import PostModal from "@/features/posts/components/PostModal";
import TrendingArtistsBar from "@/features/feed/components/TrendingArtistsBar";
import { useFeed } from "../hooks/useFeed";
import { useArtist } from "@/features/artists/hooks/useArtist";

function FeedMain(): JSX.Element {
  const { posts, loading } = useFeed();
  const { trendingArtists, loadingTrendingArtists } = useArtist();
  const [selectedPost, setSelectedPost] = useState<Post | null>(null);

  return (
    <div className="grid grid-cols-[2fr_1fr] h-screen">
      <div className="flex flex-col items-center px-4 xl:px-12 my-12 w-full overflow-y-auto">
        <div className="flex flex-col gap-4 w-full">
          <PostCreate />
          {loading ? (
            <div className="text-center py-8">Loading posts...</div>
          ) : posts.length > 0 ? (
            posts.map((post) => (
              <Post key={post.id} post={post} onSelect={() => setSelectedPost(post)} />
            ))
          ) : (
            <div className="text-center py-8">No posts found.</div>
          )}
        </div>
      </div>
      <TrendingArtistsBar trendingArtists={trendingArtists} loadingTrendingArtists={loadingTrendingArtists} />
      {selectedPost && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
            <button
              className="absolute top-3 right-3 text-gray-500 hover:text-gray-800"
              onClick={() => setSelectedPost(null)}
            >
              ✕
            </button>
            <PostModal post={selectedPost} />
          </div>
        </div>
      )}
    </div>
  );
}

export default FeedMain;