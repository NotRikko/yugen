package rikko.yugen.dto.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    private String artistName;

    private String bio;

    private String profilePictureUrl;
    private String bannerPictureUrl;

    @NotNull(message = "User ID is required")
    private Long userId;
}