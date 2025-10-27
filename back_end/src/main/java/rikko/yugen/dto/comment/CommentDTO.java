package rikko.yugen.dto.comment;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Comment;

import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        UserDTO user,
        String content,
        LocalDateTime createdAt
) {
    public CommentDTO(Comment comment) {
        this(
                comment.getId(),
                comment.getUser() != null ? new UserDTO(comment.getUser()) : null,
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}