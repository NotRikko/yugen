import React from "react";
import type { ProductDTO } from "../types";
import ProductCard from "./ProductCard";

export interface ProductGridProps {
  products: ProductDTO[];
  loading?: boolean;
  emptyMessage?: string;
  handleDelete?: (productId: number) => Promise<void>;
}

const ProductGrid: React.FC<ProductGridProps> = ({
  products,
  loading = false,
  emptyMessage = "No products found.",
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
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default ProductGrid;