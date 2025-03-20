import { useUser } from "@/UserProvider";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function DeleteAccountButton() {
    const {user, guestUser, setUser} = useUser();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    async function deleteAccount() {
        const confirmed = window.confirm("Are you sure you want to delete your account? This action cannot be undone.");
        if (!confirmed) return;
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8080/users/delete/${user.id}`, {
            mode: "cors",
            method: "DELETE"
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message);
        };

        localStorage.removeItem("user");
        setUser(guestUser);
        navigate("/home");
        } catch (error) {
            console.error("Account deletion error:", error);
        } finally {
            setLoading(false);
        }
        
    };
    return(
        <button 
            onClick={deleteAccount}
            disabled={loading}
            className="w-1/3 bg-red-600 text-white font-medium px-4 py-2 rounded-lg shadow-md 
               hover:bg-red-700 focus:ring-2 focus:ring-red-400 focus:outline-none 
               disabled:bg-red-300 disabled:cursor-not-allowed transition-all"
        >
                Delete Account
        </button>
    )
}