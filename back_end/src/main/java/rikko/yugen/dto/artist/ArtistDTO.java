package rikko.yugen.dto.artist;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Artist;

public class ArtistDTO {
    private final Long id;
    private final String artistName;
    private final String profilePictureUrl;
    private final UserDTO user;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.artistName = artist.getArtistName();
        this.profilePictureUrl = artist.getprofilePictureUrl();
        this.user= artist.getUser() != null ? new UserDTO(artist.getUser()) : null;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getprofilePictureUrl() {
        return profilePictureUrl;
    }

    public UserDTO getUser() {
        return user;
    }
}
