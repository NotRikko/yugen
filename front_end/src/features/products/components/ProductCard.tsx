import type { ProductDTO } from "../types";

interface ProductItemProps {
  product: ProductDTO | null; 
}

const ProductCard: React.FC<ProductItemProps> = ({ product}) => {
  if (!product) {
    return (
      <div className="border rounded-lg p-4 shadow-sm flex items-center justify-center h-64">
        <p className="text-gray-500">Product not available</p>
      </div>
    );
  }

  const firstImage = product.imageUrls?.[0] || "https://via.placeholder.com/300x300?text=No+Image";

  return (
    <div className="border rounded-lg p-4 shadow-sm flex flex-col gap-2">
      <img
        src={firstImage}
        alt={product.name ?? "Unnamed Product"}
        className="w-full h-48 object-cover rounded"
      />
      <h2 className="text-lg font-semibold">{product.name ?? "Unnamed Product"}</h2>
      <p className="text-gray-600">
        ${product.price != null ? product.price.toFixed(2) : "N/A"}
      </p>

  
    </div>
  );
};

export default ProductCard;