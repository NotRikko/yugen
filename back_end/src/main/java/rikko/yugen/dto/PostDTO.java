package rikko.yugen.dto;

import java.util.List;

import rikko.yugen.model.Post;

public class PostDTO {
    private final Long id;
    private final List<LikeDTO> likes;
    private final String content;
    private final ArtistDTO artist;
    private final ProductDTO product;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.likes = post.getLikes() != null
                ? post.getLikes().stream().map(LikeDTO::new).toList()
                : null; 
        this.content = post.getContent();
        this.artist = post.getArtist() != null ? new ArtistDTO(post.getArtist()) : null;
        this.product = post.getProduct() != null ? new ProductDTO(post.getProduct()) : null;
    }

    //Getters
    public Long getId() {
        return id;
    }

    public List<LikeDTO> getLikes() {
        return likes;
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
