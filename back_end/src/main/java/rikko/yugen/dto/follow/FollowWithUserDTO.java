package rikko.yugen.dto.follow;

import java.time.LocalDateTime;

public record FollowWithUserDTO(
        Long id,
        String username,
        String displayName,
        String avatarUrl,
        LocalDateTime followedAt
) {}