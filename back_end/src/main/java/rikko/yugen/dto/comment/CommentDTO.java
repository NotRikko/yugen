package rikko.yugen.dto.comment;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.dto.user.UserSummaryDTO;
import rikko.yugen.model.Comment;

import java.time.LocalDateTime;


public record CommentDTO(
        Long id,
        UserSummaryDTO user,
        String content,
        Long postId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public CommentDTO(Comment comment) {
        this(
                comment.getId(),
                comment.getUser() != null ? new UserSummaryDTO(comment.getUser()) : null,
                comment.getContent(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}