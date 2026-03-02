package rikko.yugen.dto.artist;

import rikko.yugen.model.Artist;

public record ArtistSummaryDTO(
        Long id,
        String artistName,
        String profilePictureUrl
) {
    public ArtistSummaryDTO(Artist artist) {
        this(
                artist.getId(),
                artist.getArtistName(),
                artist.getProfileImage() != null ? artist.getProfileImage().getUrl() : null
        );
    }
}