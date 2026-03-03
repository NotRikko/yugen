import { fetchClient } from "@/shared/api/fetchClient";
import type { UserDTO } from "@/features/auth/types";

interface UpdateUserPayload {
  username: string;
  displayName: string;
  email: string;
  file?: File;
}

export const userApi = {
  getCurrentUser: () => fetchClient<UserDTO>("/users/me", { method: "GET", auth: true }),

  updateUser: async (userId: number, data: UpdateUserPayload): Promise<UserDTO> => {
    const formData = new FormData();
    const { file, ...rest } = data;
    formData.append("patch", new Blob([JSON.stringify(rest)], { type: "application/json" }));
    if (file) formData.append("file", file);

    const res = await fetchClient<UserDTO>(`/users/update/${userId}`, {
      method: "PATCH",
      body: formData,
      auth: true,
    });

    if (!res) throw new Error("Failed to update user"); 
    return res;
  },

  deleteCurrentUser: async () =>
    fetchClient<void>("/users/me", {
      method: "DELETE",
      auth: true,
    }),

};