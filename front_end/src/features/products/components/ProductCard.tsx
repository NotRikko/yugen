import { useUser } from "@/features/user/useUserContext";
import type { Product } from "../types/productTypes";
import { useCart } from "@/features/cart/useCartContext";
interface ProductItemProps {
  product: Product;
}

const ProductCard: React.FC<ProductItemProps> = ({ product }) => {
  const { addToCart } = useCart();
  const { user } = useUser();
  const handleAddToCart = () => {
    addToCart(product.id, 1);
  };

  return (
    <div className="border rounded-lg p-4 shadow-sm flex flex-col gap-2">
      <img
        src={product.image}
        alt={product.name}
        className="w-full h-48 object-cover rounded"
      />
      <h2 className="text-lg font-semibold">{product.name}</h2>
      <p className="text-gray-600">${product.price.toFixed(2)}</p>
      {user.id === product.artist.user.id ? "" : 
        <button
          onClick={handleAddToCart}
          className="mt-auto bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Add to Cart
        </button>
      }
      {
        user.id === product.artist.user.id ? 
          <button
            className="mt-auto bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Edit 
          </button> : ""
      }
      
    </div>
  );
};

export default ProductCard;