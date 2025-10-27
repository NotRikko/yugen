package rikko.yugen.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
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

    @Getter
    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final UserService userService;
    private final JwtCookieHelper jwtCookieHelper;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, userDetails, refreshTokenExpiration);
    }

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

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public LoginResponseDTO refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtCookieHelper.extractRefreshToken(request);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new JwtException("Missing refresh token");
        }

        String username = extractUsername(refreshToken);
        UserDetails user = userService.getUserByUsername(username);

        if (!isTokenValid(refreshToken, user)) {
            throw new JwtException("Invalid refresh token");
        }

        String newAccessToken = generateAccessToken(user);

        return new LoginResponseDTO(
                newAccessToken,
                accessTokenExpiration
        );
    }

}
