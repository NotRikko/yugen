package rikko.yugen.dto.comment;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    private final Long id;
    private final UserDTO user;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.user = comment.getUser() != null ? new UserDTO(comment.getUser()) : null;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

    //Getters
    public Long getId() { return id; }

    public UserDTO getUser() { return user; }

    public String getContent() { return content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

}