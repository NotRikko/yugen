package rikko.yugen.dto.artist;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Artist;

public class ArtistDTO {
    private final Long id;
    private final String artistName;
    private final String image;
    private final UserDTO user;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.artistName = artist.getArtistName();
        this.image = artist.getImage();
        this.user= artist.getUser() != null ? new UserDTO(artist.getUser()) : null;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImage() {
        return image;
    }

    public UserDTO getUser() {
        return user;
    }
}
