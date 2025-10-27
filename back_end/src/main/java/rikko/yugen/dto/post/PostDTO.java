package rikko.yugen.dto.post;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Post;


public record PostDTO(
        Long id,
        Set<LikeDTO> likes,
        Set<ImageDTO> images,
        List<CommentDTO> comments,
        String content,
        ArtistDTO artist,
        ProductDTO product
) {
    public PostDTO(Post post, Set<LikeDTO> likes, Set<ImageDTO> images, List<CommentDTO> comments) {
        this(
                post.getId(),
                likes != null ? likes : new HashSet<>(),
                images != null ? images : new HashSet<>(),
                comments != null ? comments : new ArrayList<>(),
                post.getContent(),
                post.getArtist() != null ? new ArtistDTO(post.getArtist()) : null,
                post.getProduct() != null ? ProductDTO.fromProduct(post.getProduct()) : null
        );
    }

    public static PostDTO fromPost(Post post, Set<LikeDTO> likes) {
        return new PostDTO(
                post,
                likes,
                post.getImages() != null
                        ? post.getImages().stream().map(ImageDTO::new).collect(Collectors.toSet())
                        : new HashSet<>(),
                post.getComments() != null
                        ? post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}