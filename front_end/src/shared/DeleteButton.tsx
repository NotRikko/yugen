import React, { useState } from "react";
import { Button } from "@/shared/ui/button";

interface DeleteButtonProps {
  onDelete: () => Promise<void>;
  type?: string; 
  className?: string;
}

export const DeleteButton: React.FC<DeleteButtonProps> = ({
  onDelete,
  type = "item",
  className = "",
}) => {
  const [loading, setLoading] = useState(false);

  const handleClick = async () => {
    const confirmed = window.confirm(
      `Are you sure you want to delete this ${type}? This action cannot be undone.`
    );
    if (!confirmed) return;

    try {
      setLoading(true);
      await onDelete();
    } catch (err) {
      console.error(`Failed to delete ${type}:`, err);
      alert(`Failed to delete ${type}. Please try again.`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Button
      onClick={handleClick}
      disabled={loading}
      className={`bg-red-500 hover:bg-red-600 text-white ${className}`}
    >
      {loading ? "Deleting..." : `Delete ${type}`}
    </Button>
  );
};