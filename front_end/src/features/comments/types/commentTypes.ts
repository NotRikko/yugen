import type { PartialUser } from "@/features/user/types/userTypes";

export interface PartialComment {
    user: PartialUser;
    content: string;
    postedAt: string;
}