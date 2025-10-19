import { useUser } from "@/features/user/UserProvider"
import { useEffect, useState } from "react";
import { artistApi } from "../api/artistApi";
import PostCard from "@/features/posts/components/Post"; 
import type { Post } from "@/features/posts/types/postTypes";
import type { Product } from "@/features/user/types/userTypes";
const ArtistDashBoard: React.FC = () => {
    const { user } = useUser();
    const [posts, setPosts] = useState<Post[]>([]);
    const [products, setProducts] = useState<Product[]>([]);
    useEffect(() => {
        if (!user?.artistId) return;
        const artistId = user.artistId; 
        const fetchPostsAndProducts = async () => {
          try {
            const [fetchedPosts, fetchedProducts] = await Promise.all([
              artistApi.getPostsById(artistId),
              artistApi.getProducts(artistId),
            ]);
    
            setPosts(fetchedPosts ?? []);
            setProducts(fetchedProducts ?? []);
          } catch (error) {
            console.error("Failed to fetch artist data:", error);
          }
        };
    
        fetchPostsAndProducts();
      }, [user]);
    

      return (
        <div className="p-6 space-y-10">
          <header className="flex justify-between items-center">
            <div>
              <h1 className="text-3xl font-bold text-gray-800">
                Welcome back, {user?.displayName || "Artist"} 🎨
              </h1>
              <p className="text-gray-500 mt-1">Manage your posts and products</p>
            </div>
          </header>
    
          <section className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div className="bg-white shadow rounded-2xl p-5 text-center">
              <h3 className="text-sm text-gray-500">Total Posts</h3>
              <p className="text-2xl font-bold">{posts.length}</p>
            </div>
            <div className="bg-white shadow rounded-2xl p-5 text-center">
              <h3 className="text-sm text-gray-500">Products</h3>
              <p className="text-2xl font-bold">{products.length}</p>
            </div>
            <div className="bg-white shadow rounded-2xl p-5 text-center">
              <h3 className="text-sm text-gray-500">Followers</h3>
              <p className="text-2xl font-bold">—</p>
            </div>
          </section>
    
          <section>
            <h2 className="text-2xl font-semibold text-gray-800 mb-4">
              All Posts
            </h2>
    
            {posts.length === 0 ? (
              <p className="text-gray-500">No posts yet.</p>
            ) : (
                <div className="space-y-5">
                    {posts.length === 0 ? (
                    <p className="text-gray-500">No posts yet.</p>
            ) : (
                    <div className="flex flex-col gap-6">
                        {posts.map((post) => (
                        <PostCard key={post.id} post={post} />
                        ))}
                    </div>
            )}
                </div>
            )}
          </section>
        </div>
      );
}


export default ArtistDashBoard