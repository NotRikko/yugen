package rikko.yugen.dto.post;

import rikko.yugen.model.Image;
import rikko.yugen.model.Post;

import java.time.LocalDateTime;
import java.util.List;


public record PostDTO(
        Long id,
        int likeCount,
        int commentCount,
        String content,
        Long artistId,
        Long productId,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public PostDTO(Post post) {
        this(
                post.getId(),
                post.getLikes() != null ? post.getLikes().size() : 0,
                post.getComments() != null ? post.getComments().size() : 0,
                post.getContent(),
                post.getArtist() != null ? post.getArtist().getId() : null,
                post.getProduct() != null ? post.getProduct().getId() : null,
                post.getImages() != null
                        ? post.getImages().stream().map(Image::getUrl).toList()
                        : List.of(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}