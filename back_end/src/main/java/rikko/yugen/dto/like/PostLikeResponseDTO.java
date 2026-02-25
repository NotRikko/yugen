package rikko.yugen.dto.like;

public record PostLikeResponseDTO(
        int likes,
        boolean likedByCurrentUser
) {}