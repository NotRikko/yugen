import { ShoppingCart } from "lucide-react";
import { useCart } from "@/features/cart/useCartContext";
import { ProductDTO } from "../types";

interface ProductDetailProps {
  product: ProductDTO | null; 
}

export default function ProductModal({ product }: ProductDetailProps) {
  const { addToCart } = useCart();

  if (!product) {
    return (
      <div className="w-11/12 max-w-2xl mx-auto my-6 p-8 bg-white border rounded-2xl shadow-2xl text-center">
        <p className="text-gray-500 text-lg">Product not found.</p>
      </div>
    );
  }

  const handleAddToCart = async () => {
    try {
      if (!product.id) return; 
      await addToCart(product.id, 1);
    } catch (error) {
      console.error("Error adding product to cart:", error);
    }
  };

  const imageUrl =
    product.imageUrls && product.imageUrls.length > 0
      ? product.imageUrls[0]
      : "https://images.emojiterra.com/google/noto-emoji/unicode-15/color/512px/2753.png";

  return (
    <div className="relative w-11/12 max-w-2xl mx-auto my-6 p-8 bg-white border rounded-2xl shadow-2xl overflow-y-auto">
      <div className="flex justify-center mb-6">
        <img
          src={imageUrl}
          alt={product.name ?? "Product"}
          className="w-full max-h-96 object-cover rounded-xl shadow-md"
        />
      </div>

      <div className="flex flex-col gap-3">
        <h2 className="text-2xl font-semibold text-gray-900">{product.name ?? "Unnamed Product"}</h2>
        <p className="text-gray-600 text-lg">
          by {product.artist?.artistName ?? "Unknown Artist"}
        </p>
        <p className="text-xl font-bold text-blue-600">
          ${product.price ?? "N/A"}
        </p>
        <p className="text-gray-700 leading-relaxed">{product.description ?? "No description available."}</p>
      </div>

      <div className="mt-8 flex justify-center">
        <button
          onClick={handleAddToCart}
          className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-medium px-6 py-3 rounded-xl shadow-md transition-all duration-200"
        >
          <ShoppingCart className="w-5 h-5" />
          Add to Cart
        </button>
      </div>
    </div>
  );
}