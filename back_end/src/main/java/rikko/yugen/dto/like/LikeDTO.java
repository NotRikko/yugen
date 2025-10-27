package rikko.yugen.dto.like;

import java.time.LocalDateTime;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Like;

public record LikeDTO(
        Long id,
        UserDTO user,
        LocalDateTime createdAt
) {
    public LikeDTO(Like like) {
        this(
                like.getId(),
                like.getUser() != null ? new UserDTO(like.getUser()) : null,
                like.getCreatedAt()
        );
    }
}
