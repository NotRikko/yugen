import { useProductHook } from "../hooks/useProduct";
import ProductCreate from "../components/ProductCreate";
import ProductGrid from "../components/ProductGrid";

const ProductPage: React.FC = () => {
  const { products, loading, error, createProductOptimistic, deleteProductOptimistic  } = useProductHook();

  if (loading) return <p>Loading products...</p>;
  if (error) return <p className="text-red-500">{error}</p>;
  return (
    <div className="py-12 px-24 grid grid-cols-1 lg:grid-cols-3 gap-8">
      <div className="lg:col-span-1">
        <ProductCreate  onCreate={createProductOptimistic}/>
      </div>

      <div className="lg:col-span-2 flex flex-col gap-6">
        <h1 className="text-2xl font-bold mb-4">Products</h1>
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