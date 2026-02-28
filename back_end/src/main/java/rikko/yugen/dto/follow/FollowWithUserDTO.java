package rikko.yugen.dto.follow;

import java.time.LocalDateTime;

public record FollowWithUserDTO(
        Long userId,
        String username,
        String displayName,
        String avatarUrl,
        LocalDateTime followedAt
) {}