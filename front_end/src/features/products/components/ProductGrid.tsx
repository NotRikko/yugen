import React from "react";
import { Product } from "../types/productTypes";
import ProductCard from "./ProductCard";

interface ProductGridProps {
  products: Product[];
  loading?: boolean;
  emptyMessage?: string;
  handleDelete?: (productId: number) => void;
}

const ProductGrid: React.FC<ProductGridProps> = ({
  products,
  loading = false,
  emptyMessage = "No products found.",
  handleDelete,
}) => {
  if (loading) {
    return <p className="text-center text-gray-500 py-8">Loading products...</p>;
  }

  if (products.length === 0) {
    return <p className="text-center text-gray-500 py-8">{emptyMessage}</p>;
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      {products.map((product) => (
        <ProductCard key={product.id} product={product} handleDelete={handleDelete} />
      ))}
    </div>
  );
};

export default ProductGrid;