package rikko.yugen.dto.user;

import rikko.yugen.model.User;

public record UserDTO(
        Long id,
        String username,
        String displayName,
        String email,
        String image,
        Boolean isArtist,
        Long artistId
) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getProfileImage() != null ? user.getProfileImage().getUrl() : null,
                user.getIsArtist(),
                user.getArtist() != null ? user.getArtist().getId() : null
        );
    }
}