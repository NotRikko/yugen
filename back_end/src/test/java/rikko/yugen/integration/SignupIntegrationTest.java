package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void signup_createsUserAndReturns201() {
        // GIVEN
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("rikko123");
        dto.setDisplayName("Rikko");
        dto.setEmail("rikko@example.com");
        dto.setPassword("password");
        dto.setIsArtist(true);

        // WHEN
        ResponseEntity<UserDTO> response =
                restTemplate.postForEntity("/auth/signup", dto, UserDTO.class);

        // THEN
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

        Assertions.assertEquals("Rikko", artist.getArtistName());
    }
}