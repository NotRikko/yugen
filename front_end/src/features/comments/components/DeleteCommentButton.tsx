import DeleteButton from "@/shared/components/DeleteButton";

interface DeleteCommentButtonProps {
    commentId: number;
    onDelete: (id: number) => void;
}

export default function DeleteCommentButton({ commentId, onDelete }: DeleteCommentButtonProps) {
const handleDelete = async () => {
    onDelete(commentId); 
};

return <DeleteButton type="comment" onDelete={handleDelete} />;
}