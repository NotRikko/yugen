package rikko.yugen.dto;

import java.time.LocalDateTime;

import rikko.yugen.model.Like;

public class LikeDTO {
    private final Long id;
    private final PostDTO post;
    private final UserDTO user;
    private final LocalDateTime createdAt;

    public LikeDTO(Like like) {
        this.id = like.getId();
        this.post = like.getPost() != null ? new PostDTO(like.getPost()) : null; 
        this.user = like.getUser() != null ? new UserDTO(like.getUser()) : null; 
        this.createdAt = like.getCreatedAt();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public PostDTO getPost() {
        return post;
    }

    public UserDTO getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

