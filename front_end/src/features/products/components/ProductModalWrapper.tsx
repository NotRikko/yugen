import { useState, useEffect } from "react";
import type { ProductDTO } from "../types";
import { productApi } from "../api/productApi";
import ProductModal from "./ProductModal";

interface ProductModalWrapperProps {
  productId: number;
  onClose: () => void;
}

const ProductModalWrapper: React.FC<ProductModalWrapperProps> = ({ productId, onClose }) => {
  const [product, setProduct] = useState<ProductDTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProduct = async () => {
      setLoading(true);
      try {
        const result = await productApi.getProductById(productId);
        setProduct(result ?? null);
      } catch (err) {
        console.error("Failed to fetch product:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [productId]);

  const handleBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) onClose();
  };

  if (loading) return <div>Loading product...</div>;
  if (!product) return null;

  return (
    <div
      className="fixed inset-0 bg-black/50 flex justify-center items-center z-50"
      onClick={handleBackgroundClick}
    >
      <div
        className="bg-white rounded-xl shadow-lg w-full md:w-4/5 lg:w-3/4 relative max-h-[95vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <ProductModal product={product} />
      </div>
    </div>
  );
};

export default ProductModalWrapper;