package rikko.yugen.dto.user;

import rikko.yugen.model.User;

public record UserSummaryDTO(
        Long id,
        String username,
        String displayName,
        String avatarUrl
) {
    public UserSummaryDTO(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getProfileImage().getUrl()
        );
    }
}