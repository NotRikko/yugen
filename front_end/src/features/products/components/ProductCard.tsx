import { useUser } from "@/features/user/useUserContext";
import type { Product } from "../types/productTypes";
import { useCart } from "@/features/cart/useCartContext";
import DeleteProductButton from "./DeleteProductButton";

interface ProductItemProps {
  product: Product;
  handleDelete?: (productId: number) => void;
}

const ProductCard: React.FC<ProductItemProps> = ({ product, handleDelete }) => {
  const { addToCart } = useCart();
  const { user } = useUser();
  const handleAddToCart = () => {
    addToCart(product.id, 1);
  };
  const isOwner = user.id === product.artist.user.id;

  return (
    <div className="border rounded-lg p-4 shadow-sm flex flex-col gap-2">
      <img
        src={product.images?.[0]?.url || "https://via.placeholder.com/300x300?text=No+Image"}
        alt={product.name}
        className="w-full h-48 object-cover rounded"
      />
      <h2 className="text-lg font-semibold">{product.name}</h2>
      <p className="text-gray-600">${product.price.toFixed(2)}</p>
      {isOwner ? (
        <>
          <button className="mt-auto bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
            Edit
          </button>
          {handleDelete && (
            <DeleteProductButton
              productId={product.id}
              onDelete={async () => handleDelete(product.id)}
            />
          )}
        </>
      ) : (
        <button
          onClick={handleAddToCart}
          className="mt-auto bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Add to Cart
        </button>
      )}
    </div>
  );
};

export default ProductCard;