package rikko.yugen.dto.like;

public record PostLikeResponse(
        int likes,
        boolean likedByCurrentUser
) {}