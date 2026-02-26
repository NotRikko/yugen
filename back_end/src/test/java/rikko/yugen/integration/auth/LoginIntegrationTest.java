package rikko.yugen.integration.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import rikko.yugen.dto.user.LoginResponseDTO;
import rikko.yugen.dto.user.UserLoginDTO;
import rikko.yugen.integration.IntegrationTestBase;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;

class LoginIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("rikko3");
        user.setPassword(passwordEncoder.encode("test12345!"));
        userRepository.save(user);
    }

    private static UserLoginDTO validDto() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("rikko3");
        dto.setPassword("test12345!");
        return dto;
    }

    @Test
    void login_returnsLoginResponse_whenUserSuccessfullyAuthenticated() {
        UserLoginDTO dto = validDto();
        ResponseEntity<LoginResponseDTO> response =
                restTemplate.postForEntity("/auth/login", dto, LoginResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        LoginResponseDTO body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.accessToken());
        Assertions.assertFalse(body.accessToken().isBlank());

        Assertions.assertNotNull(body.accessTokenExpiresIn());
        Assertions.assertTrue(body.accessTokenExpiresIn() > 0);

    }

    @Test
    void login_returns401_whenInvalidPassword() {
        UserLoginDTO dto = validDto();
        dto.setPassword("wrongPassword");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/login", dto, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains("Invalid username or password"));
    }

    @Test
    void login_returns400_whenUsernameOrPasswordMissing() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("");
        dto.setPassword("somePassword");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/login", dto, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        dto.setUsername("rikko3");
        dto.setPassword("");
        response = restTemplate.postForEntity("/auth/login", dto, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void refreshAccessToken_returnsNewAccessToken_whenValidRefreshToken() {
        ResponseEntity<LoginResponseDTO> loginResponse =
                restTemplate.postForEntity("/auth/login", validDto(), LoginResponseDTO.class);
        String refreshToken = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assertions.assertNotNull(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, refreshToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<LoginResponseDTO> refreshResponse =
                restTemplate.exchange("/auth/refresh-token", HttpMethod.POST, requestEntity, LoginResponseDTO.class);

        Assertions.assertEquals(HttpStatus.OK, refreshResponse.getStatusCode());
        Assertions.assertNotNull(refreshResponse.getBody());
        Assertions.assertNotNull(refreshResponse.getBody().accessToken());
        Assertions.assertTrue(refreshResponse.getBody().accessTokenExpiresIn() > 0);
    }

    @Test
    void refreshAccessToken_returns401_whenMissingOrInvalidToken() {
        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/refresh-token", null, String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "refreshToken=invalidToken");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        response = restTemplate.exchange("/auth/refresh-token", HttpMethod.POST, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
