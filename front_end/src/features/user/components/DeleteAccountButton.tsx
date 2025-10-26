import { useUser } from "../useUserContext";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { userApi } from "@/features/user/api/userApi";
import { Button } from "@/shared/ui/button";

export default function DeleteAccountButton() {
  const { setUser, guestUser } = useUser();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  async function handleDeleteAccount() {
    const confirmed = window.confirm(
      "Are you sure you want to delete your account? This action cannot be undone."
    );
    if (!confirmed) return;

    try {
      setLoading(true);
      const res = await userApi.deleteCurrentUser();

      if (!res || !res.message) {
        throw new Error("Failed to delete account");
      }

      localStorage.removeItem("user");
      localStorage.removeItem("accessToken");
      setUser(guestUser);

      navigate("/");
    } catch (error) {
      console.error("Account deletion error:", error);
      alert("Failed to delete account. Please try again.");
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
      {loading ? "Deleting..." : "Delete Account"}
    </Button>
  );
}