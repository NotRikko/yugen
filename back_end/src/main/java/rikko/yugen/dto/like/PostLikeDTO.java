package rikko.yugen.dto.like;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.PostLike;

import java.time.LocalDateTime;

public record PostLikeDTO(
        Long id,
        UserDTO user,
        LocalDateTime createdAt
) {
    public PostLikeDTO(PostLike postLike) {
        this(
                postLike.getId(),
                postLike.getUser() != null ? new UserDTO(postLike.getUser(), null) : null,
                postLike.getCreatedAt()
        );
    }
}