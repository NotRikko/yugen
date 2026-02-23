package rikko.yugen.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rikko.yugen.dto.user.LoginResponseDTO;
import rikko.yugen.helpers.JwtCookieHelper;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final UserService userService;
    private final JwtCookieHelper jwtCookieHelper;

    // Helpers

    private boolean isTokenValidInternal(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    // Extraction

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired");
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    // Generation

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, userDetails, refreshTokenExpiration);
    }

    // Build

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valid

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        String type = extractTokenType(token);

        if ("refresh".equals(type)) {
            return false;
        }

        return isTokenValidInternal(token, userDetails);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        String type = extractTokenType(token);

        if (!"refresh".equals(type)) {
            return false;
        }

        return isTokenValidInternal(token, userDetails);
    }

    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }



    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public LoginResponseDTO refreshAccessToken(HttpServletRequest request) {

        String refreshToken = jwtCookieHelper.extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new JwtException("Missing refresh token");
        }

        if (!"refresh".equals(extractTokenType(refreshToken))) {
            throw new JwtException("Invalid token type");
        }

        String username = extractUsername(refreshToken);
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!isRefreshTokenValid(refreshToken, userDetails)) {
            throw new JwtException("Invalid refresh token");
        }

        String newAccessToken = generateAccessToken(userDetails);

        return new LoginResponseDTO(
                newAccessToken,
                accessTokenExpiration
        );
    }

}
