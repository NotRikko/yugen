import { useProduct } from "../useProductContext";
import ProductCard from "../components/ProductCard";
import ProductCreate from "../components/ProductCreate";
const ProductPage: React.FC = () => {
    const { products } = useProduct();
    return (
        <div className="py-12 px-24 grid grid-cols-1 lg:grid-cols-3 gap-8">
  <div className="lg:col-span-1">
    <ProductCreate />
  </div>

  <div className="lg:col-span-2 flex flex-col gap-6">
    <h1 className="text-2xl font-bold mb-4">Products</h1>
    {products && products.length > 0 ? (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    ) : (
      <p>You have no products yet.</p>
    )}
  </div>
</div>
    )
}

export default ProductPage;