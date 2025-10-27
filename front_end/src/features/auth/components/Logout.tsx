import { useEffect } from "react";
import { useUser } from "@/features/user/useUserContext";
import { useNavigate } from "react-router-dom";

export default function Logout() {
  const { handleLogout } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (handleLogout) handleLogout();
    navigate("/"); 
  }, [handleLogout, navigate]);

  return null;
}