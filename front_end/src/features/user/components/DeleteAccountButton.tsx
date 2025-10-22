import { useUser } from "@/features/user/UserProvider";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { userApi } from "@/features/user/api/userApi";

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
    <button
      onClick={handleDeleteAccount}
      disabled={loading}
      className="w-1/3 bg-red-600 text-white font-medium px-4 py-2 rounded-lg shadow-md 
        hover:bg-red-700 focus:ring-2 focus:ring-red-400 focus:outline-none 
        disabled:bg-red-300 disabled:cursor-not-allowed transition-all"
    >
      {loading ? "Deleting..." : "Delete Account"}
    </button>
  );
}