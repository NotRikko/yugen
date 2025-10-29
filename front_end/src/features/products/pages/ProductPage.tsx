import { useState } from "react";
import { useProductHook } from "../hooks/useProduct";
import ProductCreate from "../components/ProductCreate";
import ProductGrid from "../components/ProductGrid";
import { Button

 } from "@mui/material";
const ProductPage: React.FC = () => {
  const { products, loading, error, createProductOptimistic, deleteProductOptimistic } = useProductHook();
  const [showCreate, setShowCreate] = useState(false);

  if (loading) return <p>Loading products...</p>;
  if (error) return <p className="text-red-500">{error}</p>;

  return (
    <div className="py-12 px-8 lg:px-24 flex flex-col gap-8">
      <div className="flex flex-col items-center justify-between">
        <h1 className="text-2xl font-bold">Products</h1>
        <Button onClick={() => setShowCreate((prev) => !prev)}>
          {showCreate ? "Cancel" : "Create Product"}
        </Button>
      </div>

      {showCreate && (
        <div className="p-6 border rounded-2xl shadow-sm bg-white">
          <ProductCreate onCreate={createProductOptimistic} />
        </div>
      )}

      <div className="flex-1">
        <ProductGrid
          products={products}
          handleDelete={deleteProductOptimistic}
          loading={loading}
          emptyMessage="You have no products yet."
        />
      </div>
    </div>
  );
};

export default ProductPage;