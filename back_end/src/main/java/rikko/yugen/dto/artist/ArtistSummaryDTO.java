package rikko.yugen.dto.artist;

import rikko.yugen.model.Artist;

public record ArtistSummaryDTO(
        Long id,
        String username,
        String displayName,
        String profilePictureUrl
) {
    public ArtistSummaryDTO(Artist artist) {
        this(
                artist.getId(),
                artist.getUser().getUsername(),
                artist.getUser().getDisplayName(),
                artist.getProfileImage() != null ? artist.getProfileImage().getUrl() : null
        );
    }
}