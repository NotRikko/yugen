package rikko.yugen.dto.artist;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArtistUpdateDTO {

    private String artistName;

    private String bio;

    private Long profileImageId;
    private Long bannerImageId;

    public ArtistUpdateDTO(String artistName, String bio, Long profileImageId, Long bannerImageId) {
        this.artistName = artistName;
        this.bio = bio;
        this.profileImageId = profileImageId;
        this.bannerImageId = bannerImageId;
    }

}