package rikko.yugen.dto;

import rikko.yugen.model.Artist;

public class ArtistDTO {
    private final Long id;
    private final String name;
    private final String image;
    private final UserDTO user;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.image = artist.getImage();
        this.user= artist.getUser() != null ? new UserDTO(artist.getUser()) : null;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public UserDTO getUser() {
        return user;
    }
}
