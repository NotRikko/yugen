import { productApi } from "../api/productApi";
import DeleteButton
 from "@/shared/components/DeleteButton";
export default function DeleteProductButton({ productId }: { productId: number }) {
  async function handleDelete() {
    await productApi.deleteProduct(productId);
  }

  return <DeleteButton type="product" onDelete={handleDelete} />;
}