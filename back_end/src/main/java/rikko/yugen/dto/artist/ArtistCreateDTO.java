package rikko.yugen.dto.artist;

import jakarta.validation.constraints.NotBlank;


public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String artistName;
    private String profilePicureUrl;
    private String bannerPictureUrl;

    @NotBlank(message = "User ID is required")
    private Long userId;

    //Getters and Setters

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getProfilePicureUrl() {
        return profilePicureUrl;
    }

    public void setProfilePicureUrl(String profilePicureUrl) {
        this.profilePicureUrl = profilePicureUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}

