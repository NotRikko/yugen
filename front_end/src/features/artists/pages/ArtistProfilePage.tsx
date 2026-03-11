import { useArtistProfile } from "../hooks/useArtistProfile";
import { useParams } from "react-router-dom";


export default function ArtistProfilePage() {
  const { username } = useParams<{ username: string }>();

  const {
    artist,
    posts,
    products,
    loading,
  } = useArtistProfile(username);

  if (loading) {
    return <div className="p-6">Loading...</div>;
  }

  if (!artist) {
    return (
      <div className="p-6 text-center">
        <h2 className="text-xl font-semibold">Artist not found</h2>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto p-6 space-y-10">
      <div className="flex items-center gap-6 border-b pb-6">
        <img
          src={artist.profilePictureUrl || "/placeholder.png"}
          alt={artist.displayName}
          className="w-24 h-24 rounded-full object-cover"
        />
        <div>
          <h1 className="text-3xl font-bold">{artist.displayName}</h1>
          <p className="text-gray-500">@{artist.username}</p>
        </div>
      </div>

      <section>
        <h2 className="text-2xl font-semibold mb-4">Products</h2>

        {products.length === 0 ? (
          <p className="text-gray-500">No products yet.</p>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {products.map((product) => (
              <div
                key={product.id}
                className="border rounded-lg p-3 hover:shadow transition"
              >
                <img
                  src={product.imageUrls![0]}
                  alt={product.name}
                  className="h-40 w-full object-cover rounded"
                />
                <p className="mt-2 font-medium">{product.name}</p>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <h2 className="text-2xl font-semibold mb-4">Posts</h2>

        {posts.length === 0 ? (
          <p className="text-gray-500">No posts yet.</p>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            {posts.map((post) => (
              <div
                key={post.id}
                className="border rounded-lg p-3 hover:shadow transition"
              >
                {post.imageUrls?.[0] && (
                  <img
                    src={post.imageUrls[0]}
                    alt="Post"
                    className="h-40 w-full object-cover rounded"
                  />
                )}
                <p className="mt-2 text-sm line-clamp-2">
                  {post.content}
                </p>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}