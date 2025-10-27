import { commentApi } from "../api/commentApi";
import DeleteButton from "@/shared/components/DeleteButton";

export default function DeleteCommentButton({ commentId } : { commentId: number}) {
    async function handleDelete() {
        await commentApi.deleteComment(commentId);
    }

    return <DeleteButton type="comment" onDelete={handleDelete} />;
}