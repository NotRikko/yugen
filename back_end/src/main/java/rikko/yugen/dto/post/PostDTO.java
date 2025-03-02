package rikko.yugen.dto.post;

import java.util.HashSet;
import java.util.Set;

import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Post;

public class PostDTO {
    private final Long id;
    private final Set<LikeDTO> likes;
    private final Set<ImageDTO> images;
    private final String content;
    private final ArtistDTO artist;
    private final ProductDTO product;

    public PostDTO(Post post, Set<LikeDTO> likes, Set<ImageDTO> images) {
        this.id = post.getId();
        this.likes = likes != null ? likes : new HashSet<>();
        this.images = images != null ? images: new HashSet<>();
        this.content = post.getContent();
        this.artist = post.getArtist() != null ? new ArtistDTO(post.getArtist()) : null;
        this.product = post.getProduct() != null ? new ProductDTO(post.getProduct()) : null;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public Set<LikeDTO> getLikes() {
        return likes;
    }

    public Set<ImageDTO> getImages() {
        return images;
    }

    public String getContent() {
        return content;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public ProductDTO getProduct() {
        return product;
    }
}
