import DeleteButton from "@/shared/components/DeleteButton";

interface DeleteProductButtonProps {
  productId: number;
  onDelete?: () => Promise<void>;
}

export default function DeleteProductButton({ productId, onDelete }: DeleteProductButtonProps) {
  async function handleDelete() {
    if (onDelete) {
      await onDelete(); 
    } else {
      await fetch(`/api/products/${productId}`, { method: "DELETE" });
    }
  }

  return <DeleteButton type="product" onDelete={handleDelete} />;
}