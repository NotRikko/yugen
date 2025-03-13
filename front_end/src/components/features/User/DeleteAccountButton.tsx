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
            throw new Error(data.message) || "Deletion Failed";
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
        >
                Delete Account
        </button>
    )
}