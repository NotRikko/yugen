package rikko.yugen.dto.follow;

import java.time.LocalDateTime;

public class FollowDTO {
    private Long userId;
    private Long artistId;
    private LocalDateTime followedAt;

    public FollowDTO(Long userId, Long artistId, LocalDateTime followedAt) {
        this.userId = userId;
        this.artistId = artistId;
        this.followedAt = followedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }
}
