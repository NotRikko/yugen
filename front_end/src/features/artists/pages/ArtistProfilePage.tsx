import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ArtistProfileHeader from "@/features/artists/components/ArtistProfileHeader";
import Post from "@/features/posts/components/Post";
import type { PartialArtist } from "@/features/artists/types/artistTypes";
import type { Post as PostType } from "@/features/posts/types/postTypes";
import { artistApi } from "@/features/artists/api/artistApi"; 

const ArtistProfilePage: React.FC = () => {
    const { artistName } = useParams<{ artistName: string }>();
    const [artist, setArtist] = useState<PartialArtist | null>(null);
    const [posts, setPosts] = useState<PostType[]>([]);
    const [loading, setLoading] = useState(true);
  
    useEffect(() => {
      const fetchArtistData = async () => {
        try {
          if (!artistName) return;
  
          const [artistData, artistPosts] = await Promise.all([
            artistApi.getByArtistName(artistName),
            artistApi.getPostsByArtistName(artistName),
          ]);
  
          setArtist(artistData);
          setPosts(artistPosts ?? []);
        } catch (err) {
          console.error("Error fetching artist profile:", err);
        } finally {
          setLoading(false);
        }
      };
  
      fetchArtistData();
    }, [artistName]);
  
    if (loading) {
      return (
        <div className="flex justify-center items-center h-screen text-gray-400">
          Loading artist profile...
        </div>
      );
    }
  
    if (!artist) {
      return (
        <div className="flex justify-center items-center h-screen text-red-500">
          Artist not found.
        </div>
      );
    }
  
    return (
      <div className="min-h-screen bg-neutral-950 text-white flex flex-col items-center">
        <div className="w-full max-w-5xl">
          <ArtistProfileHeader artist={artist} />
        </div>
  
        <div className="w-full max-w-4xl mt-10 mb-20">
          <h2 className="text-xl font-semibold mb-4 px-4">Posts</h2>
  
          {posts.length > 0 ? (
            <div className="flex flex-col gap-6">
              {posts.map((post) => (
                <Post key={post.id} post={post} />
              ))}
            </div>
          ) : (
            <p className="text-gray-500 text-center mt-10">No posts yet.</p>
          )}
        </div>
      </div>
    );
  };
  
  export default ArtistProfilePage;