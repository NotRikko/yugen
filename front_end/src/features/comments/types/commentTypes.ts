import type { PartialUser } from "@/features/user/types/userTypes";

export interface PartialComment {
    id: number;
    user: PartialUser;
    content: string;
    postedAt: string;
}