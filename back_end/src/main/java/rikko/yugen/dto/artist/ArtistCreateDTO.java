package rikko.yugen.dto.artist;

import jakarta.validation.constraints.NotBlank;


public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String artistName;
    private String image;

    @NotBlank(message = "User ID is required")
    private Long userId;

    //Getters and Setters

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}

