package rikko.yugen.dto.like;

import java.time.LocalDateTime;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Like;

public class LikeDTO {
    private final Long id;
    private final UserDTO user;
    private final LocalDateTime createdAt;

    public LikeDTO(Like like) {
        this.id = like.getId();
        this.user = like.getUser() != null ? new UserDTO(like.getUser()) : null; 
        this.createdAt = like.getCreatedAt();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

