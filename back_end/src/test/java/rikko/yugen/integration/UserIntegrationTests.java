package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import rikko.yugen.dto.PageResponseDTO;
import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.integration.base.IntegrationTestBase;
import rikko.yugen.model.*;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.FollowRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.service.JwtService;

class UserIntegrationTests extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtService jwtService;

    private User user;
    private User otherUser;

    @BeforeEach
    void cleanUpAndSetUp() {
        followRepository.deleteAll();
        commentRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("rikko");
        user.setPassword("test123!");
        user.setRole(Role.USER);
        user.setDisplayName("Rikko");
        user.setEmail("rikko@test.com");

        otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("test123!");
        otherUser.setRole(Role.USER);
        otherUser.setDisplayName("Other User");
        otherUser.setEmail("other@test.com");

        user = userRepository.save(user);
        otherUser = userRepository.save(otherUser);
    }

    // Helpers

    private String getToken(User user, Role role) {
        return jwtService.generateAccessToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(role.name())
                        .build()
        );
    }

    private HttpHeaders createJsonHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    private ResponseEntity<UserDTO> createUser(UserCreateDTO dto) {
        return restTemplate.postForEntity("/users", dto, UserDTO.class);
    }

    // Get tests

    @Test
    void getAllUsers_returns200_andUsers() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.USER);
        HttpHeaders headers = createJsonHeaders(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PageResponseDTO<UserDTO>> response =
                restTemplate.exchange(
                        "/users",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PageResponseDTO<UserDTO> body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.content().isEmpty());

        UserDTO first = body.content().get(0);
        Assertions.assertNotNull(first.id());
        Assertions.assertNotNull(first.username());
    }

    @Test
    void getUserById_returns200_whenExists() {
        ResponseEntity<UserDTO> response =
                restTemplate.getForEntity("/users/" + user.getId(), UserDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user.getUsername(), response.getBody().username());
    }

    @Test
    void getUserByUsername_returns200_whenExists() {
        ResponseEntity<UserDTO> response =
                restTemplate.getForEntity("/users/by-username/" + user.getUsername(), UserDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user.getId(), response.getBody().id());
    }

    @Test
    void getUserByDisplayName_returns200_whenExists() {
        ResponseEntity<UserDTO> response =
                restTemplate.getForEntity("/users/by-display-name/" + user.getDisplayName(), UserDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user.getId(), response.getBody().id());
    }

    @Test
    void getAuthenticatedUser_returns200_whenAuthenticated() {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));

        ResponseEntity<UserDTO> response =
                restTemplate.exchange(
                        "/users/me",
                        HttpMethod.GET,
                        request,
                        UserDTO.class
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user.getId(), response.getBody().id());
    }

    // Create tests

    @Test
    void createUser_asAdmin_returns201() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.ADMIN);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("adminCreated");
        dto.setDisplayName("Admin Created");
        dto.setEmail("admin@test.com");
        dto.setPassword("password123!!!");
        dto.setIsArtist(false);

        HttpEntity<UserCreateDTO> request =
                new HttpEntity<>(dto, createJsonHeaders(token));

        ResponseEntity<UserDTO> response =
                restTemplate.postForEntity("/users", request, UserDTO.class);
        System.out.println(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createUser_asRegularUser_returns403() {
        String token = getToken(user, Role.USER);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("failUser");
        dto.setDisplayName("Fail User");
        dto.setEmail("fail@test.com");
        dto.setPassword("password123!!!");
        dto.setIsArtist(false);

        HttpEntity<UserCreateDTO> request =
                new HttpEntity<>(dto, createJsonHeaders(token));

        ResponseEntity<String> response =
                restTemplate.postForEntity("/users", request, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void createUser_asAdmin_duplicateUsername_returns409() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.ADMIN);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(user.getUsername());
        dto.setDisplayName("Duplicate Username");
        dto.setEmail("unique@test.com");
        dto.setPassword("password123!!!");
        dto.setIsArtist(false);

        HttpEntity<UserCreateDTO> request =
                new HttpEntity<>(dto, createJsonHeaders(token));

        ResponseEntity<String> response =
                restTemplate.postForEntity("/users", request, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_asAdmin_duplicateEmail_returns409() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.ADMIN);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("uniqueUsername");
        dto.setDisplayName("Duplicate Email");
        dto.setEmail(user.getEmail());
        dto.setPassword("password123!!!");
        dto.setIsArtist(false);

        HttpEntity<UserCreateDTO> request =
                new HttpEntity<>(dto, createJsonHeaders(token));

        ResponseEntity<String> response =
                restTemplate.postForEntity("/users", request, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_asAdmin_invalidDto_returns400() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.ADMIN);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setDisplayName("Invalid");
        dto.setEmail("invalid@test.com");
        dto.setPassword("password123!!!");
        dto.setIsArtist(false);

        HttpEntity<UserCreateDTO> request =
                new HttpEntity<>(dto, createJsonHeaders(token));

        ResponseEntity<String> response =
                restTemplate.postForEntity("/users", request, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Delete tests

    @Test
    void deleteCurrentUser_returns204_whenAuthenticated() {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));

        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/users/me",
                        HttpMethod.DELETE,
                        request,
                        Void.class
                );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    void deleteUser_asAdmin_returns204() {
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        String token = getToken(user, Role.ADMIN);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));

        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/users/" + otherUser.getId(),
                        HttpMethod.DELETE,
                        request,
                        Void.class
                );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertFalse(userRepository.existsById(otherUser.getId()));
    }

}