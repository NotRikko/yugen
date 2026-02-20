package rikko.yugen.dto.artist;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Artist;

public record ArtistDTO(
        Long id,
        String artistName,
        String profilePictureUrl,
        String bannerPictureUrl,
        UserDTO user
) {
    public ArtistDTO(Artist artist) {
        this(
                artist.getId(),
                artist.getArtistName(),
                artist.getProfileImage() != null ? artist.getProfileImage().getUrl() : null,
                artist.getBannerImage() != null ? artist.getBannerImage().getUrl() : null,
                artist.getUser() != null ? new UserDTO(artist.getUser()) : null
        );
    }
}