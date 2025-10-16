package rikko.yugen.dto.artist;

import jakarta.validation.constraints.NotBlank;


public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String artistName;
    private String profilePictureUrl;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBannerPictureUrl() { return bannerPictureUrl; }

    public void setBannerPictureUrl(String bannerPictureUrl) { this.bannerPictureUrl = bannerPictureUrl; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}

