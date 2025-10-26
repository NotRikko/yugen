import { useUser } from "@/features/user/useUserContext";
import { useState } from "react";
import { productApi } from "../api/productApi";
import { Button } from "@/shared/ui/button";

export default function DeleteProductButton() {
  const { setUser, guestUser } = useUser();
  const [loading, setLoading] = useState(false);

  async function handleDeleteAccount() {

    try {
      setLoading(true);
      const res = await productApi

      if (!res || !res.message) {
        throw new Error("Failed to delete Product.");
      }

    } catch (error) {
      console.error("Product deletion error:", error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Button
      onClick={handleDeleteAccount}
      disabled={loading}
      variant="destructive"
      size="lg"
      className="w-1/3"
    >
      {loading ? "Deleting..." : "Delete"}
    </Button>
  );
}