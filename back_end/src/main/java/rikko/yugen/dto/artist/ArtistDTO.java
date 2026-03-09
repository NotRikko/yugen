package rikko.yugen.dto.artist;

import rikko.yugen.model.Artist;

public record ArtistDTO(
        Long id,
        String profilePictureUrl,
        String bannerPictureUrl,
        String bio,
        Long userId
) {
    public ArtistDTO(Artist artist) {
        this(
                artist.getId(),
                artist.getProfileImage() != null ? artist.getProfileImage().getUrl() : null,
                artist.getBannerImage() != null ? artist.getBannerImage().getUrl() : null,
                artist.getBio(),
                artist.getUser() != null ? artist.getUser().getId() : null
        );
    }
}