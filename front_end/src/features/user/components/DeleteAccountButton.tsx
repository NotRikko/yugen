import { userApi } from "@/features/user/api/userApi";
import { useUser } from "../useUserContext";
import { useNavigate } from "react-router-dom";
import DeleteButton from "@/shared/components/DeleteButton";

export default function DeleteAccountButton() {
  const { setUser, guestUser } = useUser();
  const navigate = useNavigate();

  async function handleDelete() {
    await userApi.deleteCurrentUser();
    localStorage.removeItem("user");
    localStorage.removeItem("accessToken");
    setUser(guestUser);
    navigate("/");
  }

  return <DeleteButton type="account" onDelete={handleDelete} />;
}