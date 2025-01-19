package rikko.yugen.dto;

import rikko.yugen.model.Artist;

public class ArtistDTO {
    private Long id;
    private String name;
    private String image;
    private String username; 

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.image = artist.getImage();
        this.username = artist.getUser() != null ? artist.getUser().getUsername() : null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }
}
