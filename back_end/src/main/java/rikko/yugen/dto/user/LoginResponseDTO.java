package rikko.yugen.dto.user;

public record LoginResponseDTO(String accessToken, String refreshToken, Long accessTokenExpiresIn,
                               Long refreshTokenExpiresIn) {
}
