package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import rikko.yugen.dto.like.PostLikeDTO;
import rikko.yugen.dto.like.PostLikeResponseDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
import rikko.yugen.model.Role;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class PostIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtService jwtService;

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

    private User user;
    private Artist artist;
    private Post savedPost;

    @BeforeEach
    void cleanUpAndSetUp() {
        postRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("rikko3");
        user.setPassword("test12345!");
        userRepository.save(user);

        artist = new Artist();
        artist.setArtistName("test");
        artist.setUser(user);
        artistRepository.save(artist);

        Post post = new Post();
        post.setArtist(artist);
        post.setContent("Existing post");
        post.setCreatedAt(LocalDateTime.now());
        savedPost = postRepository.save(post);
    }

    private PostCreateDTO validDto() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.setArtistId(artist.getId());
        dto.setContent("This is a test post");
        return dto;
    }

    // Helpers

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String getToken(User user, Role role) {
        user.setRole(role);
        userRepository.save(user);
        return jwtService.generateAccessToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(role.name())
                        .build()
        );
    }

    // Create post tests

    @Test
    void createPost_returns201_whenUserIsArtist() {
        PostCreateDTO dto = validDto();

        user.setRole(Role.ARTIST);
        userRepository.save(user);

        String token = getToken(user, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("post", new HttpEntity<>(dto, createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<PostDTO> response =
                restTemplate.postForEntity("/posts", request, PostDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        PostDTO post = response.getBody();
        Assertions.assertNotNull(post);
        Assertions.assertEquals(dto.getContent(), post.content());
        Assertions.assertEquals(artist.getId(), post.artistId());
    }

    @Test
    void createPost_returns403_whenUserIsNotArtist() {
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("post", new HttpEntity<>(validDto(), createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/posts", request, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Post get tests

    @Test
    void getAllPosts_returns200_andPosts() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/posts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);

        List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
        Assertions.assertFalse(content.isEmpty());
        Assertions.assertTrue(content.get(0).containsKey("content"));
        Assertions.assertTrue(content.get(0).containsKey("artistId"));
    }



    @Test
    void getPostById_returns200_whenPostExists() {
        ResponseEntity<PostDTO> response =
                restTemplate.getForEntity("/posts/" + savedPost.getId(), PostDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(savedPost.getContent(), response.getBody().content());
    }

    @Test
    void getPostById_returns404_whenPostDoesNotExist() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/posts/99999", String.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPostsByArtist_returns200_andPosts() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/posts/artist/" + artist.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);

        List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
        Assertions.assertFalse(content.isEmpty());

        Map<String, Object> firstPost = content.get(0);
        Assertions.assertEquals(artist.getId().intValue(), ((Number) firstPost.get("artistId")).intValue());
    }

    // Post update tests

    @Test
    void updatePost_returns200_whenUserIsOwner() {
        String token = getToken(user, Role.ARTIST);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setContent("Updated content");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<PostUpdateDTO> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<PostDTO> response =
                restTemplate.exchange("/posts/" + savedPost.getId(), HttpMethod.PUT, request, PostDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated content", response.getBody().content());
    }

    @Test
    void updatePost_returns403_whenUserIsNotOwner() {
        User otherUser = new User();
        otherUser.setUsername("other");
        otherUser.setPassword("pass");
        userRepository.save(otherUser);

        String token = getToken(otherUser, Role.ARTIST);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setContent("Hacked content");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<PostUpdateDTO> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/posts/" + savedPost.getId(), HttpMethod.PUT, request, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Post delete tests

    @Test
    void deletePost_returns204_whenUserIsOwner() {
        String token = getToken(user, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response =
                restTemplate.exchange("/posts/" + savedPost.getId(), HttpMethod.DELETE, request, Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertFalse(postRepository.findById(savedPost.getId()).isPresent());
    }

    @Test
    void deletePost_returns403_whenUserIsNotOwner() {
        User otherUser = new User();
        otherUser.setUsername("other");
        otherUser.setPassword("pass");
        userRepository.save(otherUser);

        String token = getToken(otherUser, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/posts/" + savedPost.getId(), HttpMethod.DELETE, request, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Post likes get tests

    @Test
    void toggleLike_returns200_andPostLikeResponseDTO() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PostLikeResponseDTO> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/like",
                HttpMethod.POST,
                request,
                PostLikeResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        PostLikeResponseDTO body = response.getBody();
        Assertions.assertNotNull(body);

        Assertions.assertTrue(body.likedByCurrentUser());
        Assertions.assertEquals(1, body.likes());
    }

    @Test
    void toggleLike_returns200_andPostLikeResponseDTO_unlike() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/like",
                HttpMethod.POST,
                request,
                PostLikeResponseDTO.class
        );

        ResponseEntity<PostLikeResponseDTO> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/like",
                HttpMethod.POST,
                request,
                PostLikeResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        PostLikeResponseDTO body = response.getBody();
        Assertions.assertNotNull(body);

        Assertions.assertFalse(body.likedByCurrentUser());
        Assertions.assertEquals(0, body.likes());
    }

    // Comments get test

    @Test
    void getComments_returns200_andCommentsPage() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);
    }
}