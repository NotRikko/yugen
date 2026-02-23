package rikko.yugen.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import rikko.yugen.dto.user.LoginResponseDTO;
import rikko.yugen.helpers.JwtCookieHelper;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtCookieHelper jwtCookieHelper;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    private final String SECRET =
            Base64.getEncoder().encodeToString(
                    "my-very-secure-secret-key-my-very-secure-secret-key".getBytes()
            );

    @BeforeEach
    void setUp() throws Exception {
        userDetails = User
                .withUsername("test@example.com")
                .password("password")
                .authorities("USER")
                .build();

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 1000L * 60);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 1000L * 120);
    }

    // Token Generation tests

    @Nested
    class tokenGenerationTests {
        @Test
        void generateAccessToken_shouldCreateValidToken() {
            String token = jwtService.generateAccessToken(userDetails);

            assertNotNull(token);
            assertTrue(jwtService.isAccessTokenValid(token, userDetails));
        }

        @Test
        void generateRefreshToken_shouldCreateValidRefreshToken() {
            String token = jwtService.generateRefreshToken(userDetails);

            assertNotNull(token);
            assertTrue(jwtService.isRefreshTokenValid(token, userDetails));
        }
    }


    // Access Token Validation tests

    @Nested
    class accessTokenValidationTests {
        @Test
        void isAccessTokenValid_shouldReturnFalse_forRefreshToken() {
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            assertFalse(jwtService.isAccessTokenValid(refreshToken, userDetails));
        }

        @Test
        void isAccessTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
            String token = jwtService.generateAccessToken(userDetails);

            UserDetails otherUser = User
                    .withUsername("other@example.com")
                    .password("password")
                    .authorities("USER")
                    .build();

            assertFalse(jwtService.isAccessTokenValid(token, otherUser));
        }
    }


    // Refresh access token tests

    @Nested
    class refreshAccessTokenTests {
        @Test
        void refreshAccessToken_shouldReturnNewAccessToken_whenValidRefreshToken() {
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            when(jwtCookieHelper.extractRefreshToken(request))
                    .thenReturn(refreshToken);

            when(userService.loadUserByUsername("test@example.com"))
                    .thenReturn(userDetails);

            LoginResponseDTO response = jwtService.refreshAccessToken(request);

            assertNotNull(response);
            assertNotNull(response.accessToken());
        }

        @Test
        void refreshAccessToken_shouldThrow_whenMissingToken() {
            when(jwtCookieHelper.extractRefreshToken(request))
                    .thenReturn(null);

            assertThrows(JwtException.class,
                    () -> jwtService.refreshAccessToken(request));
        }

        @Test
        void refreshAccessToken_shouldThrow_whenInvalidTokenType() {
            String accessToken = jwtService.generateAccessToken(userDetails);

            when(jwtCookieHelper.extractRefreshToken(request))
                    .thenReturn(accessToken);

            assertThrows(JwtException.class,
                    () -> jwtService.refreshAccessToken(request));
        }

        // Login response test

        @Test
        void generateLoginResponseWithRefreshToken_shouldReturnLoginResponseAndSetCookie() {
            HttpServletResponse response = mock(HttpServletResponse.class);
            UserDetails userDetails = mock(UserDetails.class);

            when(userDetails.getUsername()).thenReturn("testuser");

            LoginResponseDTO result = jwtService.generateLoginResponseWithRefreshToken(response, userDetails);

            assertNotNull(result.accessToken());
            assertTrue(result.accessTokenExpiresIn() > 0);
        }
    }

}