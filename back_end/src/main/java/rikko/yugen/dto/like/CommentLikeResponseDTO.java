package rikko.yugen.dto.like;

public record CommentLikeResponseDTO(
    int likes,
    boolean likedByCurrentUser
) {}
