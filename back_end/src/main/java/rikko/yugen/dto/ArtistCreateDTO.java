package rikko.yugen.dto;

import jakarta.validation.constraints.NotBlank;


public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;
    private String image;

    @NotBlank(message = "User ID is required")
    private Long userId;

    //Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

