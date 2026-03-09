package rikko.yugen.dto.artist;

import lombok.Data;

@Data
public class ArtistCreateDTO {

    private String bio;

    private String profilePictureUrl;
    private String bannerPictureUrl;

}