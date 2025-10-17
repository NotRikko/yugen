import { fetchClient } from "@/shared/api/fetchClient";
import { User } from "../types/userTypes";

interface UpdateUserPayload {
  username: string;
  displayName: string;
  email: string;
  file?: File;
}

export const userApi = {
  getCurrentUser: () => fetchClient<User>("/users/me", { method: "GET", auth: true }),

  updateUser: async (userId: number, data: UpdateUserPayload): Promise<User> => {
    const formData = new FormData();
    const { file, ...rest } = data;
    formData.append("patch", new Blob([JSON.stringify(rest)], { type: "application/json" }));
    if (file) formData.append("file", file);

    const res = await fetchClient<User>(`/users/update/${userId}`, {
      method: "PATCH",
      body: formData,
      auth: true,
    } as any);

    if (!res) throw new Error("Failed to update user"); 
    return res;
  },

};