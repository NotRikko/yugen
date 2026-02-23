package rikko.yugen.dto.comment;

import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Comment;
import rikko.yugen.model.Post;

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
                comment.getUser() != null ? new UserDTO(comment.getUser()) : null,
                comment.getContent(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getCreatedAt()
        );
    }
}

