package rikko.yugen.dto.artist;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String artistName;

    private String bio;

    private String profilePictureUrl;
    private String bannerPictureUrl;

}