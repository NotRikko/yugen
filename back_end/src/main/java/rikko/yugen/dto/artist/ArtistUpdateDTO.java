package rikko.yugen.dto.artist;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArtistUpdateDTO {

    private String bio;

    private Long profileImageId;
    private Long bannerImageId;

    public ArtistUpdateDTO(String bio, Long profileImageId, Long bannerImageId) {
        this.bio = bio;
        this.profileImageId = profileImageId;
        this.bannerImageId = bannerImageId;
    }

}