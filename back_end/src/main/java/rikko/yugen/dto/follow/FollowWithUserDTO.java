package rikko.yugen.dto.follow;

import java.time.LocalDateTime;

public class FollowWithUserDTO {
    private Long id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private LocalDateTime followedAt;

    public FollowWithUserDTO(Long id, String username, String displayName, String avatarUrl, LocalDateTime followedAt) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.followedAt = followedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }
}