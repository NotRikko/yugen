package rikko.yugen.dto.post;

import rikko.yugen.dto.artist.ArtistSummaryDTO;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public record FeedPostDTO(
        Long id,
        String content,
        int likeCount,
        int commentCount,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArtistSummaryDTO artist,
        Long productId
) {
    public FeedPostDTO(Post post) {
        this(
                post.getId(),
                post.getContent(),
                post.getLikes() != null ? post.getLikes().size() : 0,
                post.getComments() != null ? post.getComments().size() : 0,
                post.getImages() != null
                        ? post.getImages().stream().map(Image::getUrl).toList()
                        : List.of(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getArtist() != null ? new ArtistSummaryDTO(post.getArtist()) : null,
                post.getProduct() != null ? post.getProduct().getId() : null
        );
    }
}