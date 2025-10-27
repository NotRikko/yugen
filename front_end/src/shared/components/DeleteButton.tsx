import { useState } from "react";
import { Button } from "@/shared/ui/button";

interface DeleteButtonProps {
  type: "account" | "product" | "comment";
  onDelete: () => Promise<void>;
  confirmMessage?: string;
}

export default function DeleteButton({
  type,
  onDelete,
  confirmMessage,
}: DeleteButtonProps) {
  const [loading, setLoading] = useState(false);

  async function handleDelete() {
    const confirmed = window.confirm(
      confirmMessage ||
        `Are you sure you want to delete this ${type}? This action cannot be undone.`
    );
    if (!confirmed) return;

    try {
      setLoading(true);
      await onDelete();
    } catch (error) {
      console.error(`${type} deletion error:`, error);
      alert(`Failed to delete ${type}. Please try again.`);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Button
      onClick={handleDelete}
      disabled={loading}
      variant="destructive"
      size="lg"
      className="w-1/3"
    >
      {loading ? `Deleting ${type}...` : `Delete ${type}`}
    </Button>
  );
}