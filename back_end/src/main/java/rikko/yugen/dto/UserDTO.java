package rikko.yugen.dto;

import rikko.yugen.model.User;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String displayName;
    private final String email;
    private final String image;
    private final Boolean isArtist;
    private final Long artistId;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.image = user.getImage();
        this.isArtist = user.getIsArtist();
        this.artistId = user.getArtist() != null ? user.getArtist().getId() : null;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public Boolean getIsArtist() {
        return isArtist;
    }

    public Long getArtistId() {
        return artistId;
    }
}
