package rikko.yugen.dto.post;

import rikko.yugen.dto.artist.ArtistSummaryDTO;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailsDTO(
        Long id,
        String content,
        int likeCount,
        int commentCount,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArtistSummaryDTO artist,
        List<CommentDTO> comments,
        Long productId
) {
    public PostDetailsDTO(Post post) {
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
                post.getComments() != null
                        ? post.getComments().stream().map(CommentDTO::new).toList()
                        : List.of(),
                post.getProduct() != null ? post.getProduct().getId() : null
        );
    }
}