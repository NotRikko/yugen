package rikko.yugen.dto.post;

import rikko.yugen.model.Post;

import java.time.LocalDateTime;

public record PostSummaryDTO(
        Long id,
        String contentSnippet,
        int likeCount,
        int commentCount,
        Long artistId,
        String artistName,
        LocalDateTime createdAt
) {
    public PostSummaryDTO(Post post) {
        this(
                post.getId(),
                post.getContent() != null && post.getContent().length() > 100
                        ? post.getContent().substring(0, 100) + "..."
                        : post.getContent(),
                post.getLikes() != null ? post.getLikes().size() : 0,
                post.getComments() != null ? post.getComments().size() : 0,
                post.getArtist() != null ? post.getArtist().getId() : null,
                post.getArtist() != null ? post.getArtist().getArtistName() : null,
                post.getCreatedAt()
        );
    }
}