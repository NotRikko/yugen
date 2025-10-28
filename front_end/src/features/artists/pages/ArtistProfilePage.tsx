import { useState } from "react";
import { useParams } from "react-router-dom";
import ArtistProfileHeader from "@/features/artists/components/ArtistProfileHeader";
import Post from "@/features/posts/components/Post";
import ProductCard from "@/features/products/components/ProductCard";
import { useArtistProfile } from "../hooks/useArtistProfile";
import { PostDTO } from "@/features/posts/types/postTypes";
import PostModalWrapper from "@/features/posts/components/PostModalWrapper";

const ArtistProfilePage: React.FC = () => {
  const { artistName } = useParams<{ artistName: string }>();
  const [activeTab, setActiveTab] = useState<"posts" | "products">("posts");
  const [ selectedPost, setSelectedPost] = useState<PostDTO | null>(null);

  const {
    artist,
    posts,
    products,
    loading,
    deletePostOptimistic,
    deleteProductOptimistic,
  } = useArtistProfile(artistName);

  if (loading) return <div>Loading artist profile...</div>;
  if (!artist) return <div>Artist not found.</div>;

  return (
    <div className="min-h-screen bg-white text-gray-800 flex flex-col items-center">
      <ArtistProfileHeader artist={artist} />

      <div className="flex gap-4 mt-6">
        <button
          onClick={() => setActiveTab("posts")}
          className={activeTab === "posts" ? "bg-blue-500 text-white" : "bg-gray-200"}
        >
          Posts
        </button>
        <button
          onClick={() => setActiveTab("products")}
          className={activeTab === "products" ? "bg-blue-500 text-white" : "bg-gray-200"}
        >
          Products
        </button>
      </div>

      <div className="w-full max-w-4xl mt-10 mb-20">
        {activeTab === "posts" ? (
          posts.length ? (
            <div className="flex flex-col gap-6">
              {posts.map((post) => (
                <Post key={post.id} post={post} onSelect={() => setSelectedPost(post)} onDelete={() => deletePostOptimistic(post.id)} />
              ))}
            </div>
          ) : (
            <p className="text-gray-500 text-center mt-10">No posts yet.</p>
          )
        ) : products.length ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {products.map((product) => (
              <ProductCard
                key={product.id}
                product={product}
                handleDelete={deleteProductOptimistic}
              />
            ))}
          </div>
        ) : (
          <p className="text-gray-500 text-center mt-10">No products yet.</p>
        )}
      </div>
      {selectedPost && (
              <div
                className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
              >
                <div className="bg-white rounded-xl shadow-lg w-11/12 md:w-3/4 lg:w-1/2 relative max-h-[90vh] overflow-y-auto">
                <PostModalWrapper post={selectedPost} onClose={() => setSelectedPost(null)} />
                </div>
              </div>
            )}
    </div>
  );
};

export default ArtistProfilePage;