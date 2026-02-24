package rikko.yugen.dto.like;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.CommentLike;

import java.time.LocalDateTime;

public record CommentLikeDTO(
        Long id,
        UserDTO user,
        LocalDateTime createdAt
) {
    public CommentLikeDTO(CommentLike commentLike) {
        this(
                commentLike.getId(),
                commentLike.getUser() != null ? new UserDTO(commentLike.getUser(), null) : null,
                commentLike.getCreatedAt()
        );
    }
}