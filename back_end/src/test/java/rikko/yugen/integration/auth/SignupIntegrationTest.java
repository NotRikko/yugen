package rikko.yugen.integration.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;

import java.util.function.Consumer;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class SignupIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16.2")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void cleanUp() {
        artistRepository.deleteAll();
        userRepository.deleteAll();
    }

    private static UserCreateDTO validDto() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("rikko123");
        dto.setDisplayName("Rikko");
        dto.setEmail("rikko@example.com");
        dto.setPassword("password1!");
        dto.setIsArtist(false);
        return dto;
    }

    @Test
    void signup_createsUserAndArtistAndReturns201() {
        UserCreateDTO dto = validDto();
        dto.setIsArtist(true);

        ResponseEntity<UserDTO> response =
                restTemplate.postForEntity("/auth/signup", dto, UserDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserDTO body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("rikko123", body.username());
        Assertions.assertEquals("Rikko", body.displayName());
        Assertions.assertEquals("rikko@example.com", body.email());

        User savedUser = userRepository.findByUsername("rikko123")
                .orElseThrow();

        Assertions.assertTrue(savedUser.getIsArtist());

        Artist artist = artistRepository
                .findByUserId(savedUser.getId())
                .orElseThrow();

        Assertions.assertEquals("Rikko",
                artist.getArtistName(),
                "Artist should be created when setIsArtist is true.");
    }

    @Test
    void signup_createsUserAndNotArtistAndReturns201() {
        UserCreateDTO dto = validDto();
        dto.setIsArtist(false);

        ResponseEntity<UserDTO> response =
                restTemplate.postForEntity("/auth/signup", dto, UserDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserDTO body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("rikko123", body.username());
        Assertions.assertEquals("Rikko", body.displayName());
        Assertions.assertEquals("rikko@example.com", body.email());

        User savedUser = userRepository.findByUsername("rikko123")
                .orElseThrow();

        Assertions.assertFalse(savedUser.getIsArtist());

        Assertions.assertTrue(
                artistRepository.findByUserId(savedUser.getId()).isEmpty(),
                "Artist should not be created when setIsArtist is false."
        );
    }

    // Invalid sign up test arguments

    static Stream<Arguments> invalidSignupRequests() {
        return Stream.of(
                Arguments.of("missing username", (Consumer<UserCreateDTO>) dto -> dto.setUsername(null)),
                Arguments.of("missing email", (Consumer<UserCreateDTO>) dto -> dto.setEmail(null)),
                Arguments.of("missing password", (Consumer<UserCreateDTO>) dto -> dto.setPassword(null)),
                Arguments.of("missing displayName", (Consumer<UserCreateDTO>) dto -> dto.setDisplayName(null))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidSignupRequests")
    void signup_returns400_whenRequiredFieldMissing(
            String description,
            Consumer<UserCreateDTO> modifier
    ) {
        UserCreateDTO dto = validDto();
        modifier.accept(dto);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", dto, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(
                0,
                userRepository.count(),
                "User should not be created when validation fails"
        );
    }

    @Test
    void signup_failsWithDuplicateUsername() {
        UserCreateDTO dto = validDto();
        restTemplate.postForEntity("/auth/signup", dto, UserDTO.class);

        UserCreateDTO duplicate = validDto();
        duplicate.setEmail("other@example.com");
        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", duplicate, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void signup_failsWithDuplicateEmail() {
        UserCreateDTO dto = validDto();
        restTemplate.postForEntity("/auth/signup", dto, UserDTO.class);

        UserCreateDTO duplicate = validDto();
        duplicate.setUsername("rikko456");
        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", duplicate, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void signup_returns400_forInvalidEmail() {
        UserCreateDTO dto = validDto();
        dto.setEmail("not-an-email");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", dto, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(
                0,
                userRepository.count(),
                "User should not be created when email is invalid"
        );
    }

    @Test
    void signup_returns400_forInvalidPasswordLength() {
        UserCreateDTO dto = validDto();
        dto.setPassword("rikko");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", dto, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(
                0,
                userRepository.count(),
                "User should not be created when password length is invalid"
        );
    }

    @Test
    void signup_returns400_forInvalidPasswordFormat() {
        UserCreateDTO dto = validDto();
        dto.setPassword("rikko1345");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/auth/signup", dto, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(
                0,
                userRepository.count(),
                "User should not be created when password length is invalid"
        );
    }
}