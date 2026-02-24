package rikko.yugen.dto.comment;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Comment;

import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        UserDTO user,
        String content,
        Long postId,
        LocalDateTime createdAt
) {
    public CommentDTO(Comment comment) {
        this(
                comment.getId(),
                comment.getUser() != null ? new UserDTO(comment.getUser(), null) : null,
                comment.getContent(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getCreatedAt()
        );
    }
}