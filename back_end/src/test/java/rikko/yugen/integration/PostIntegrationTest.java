package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import rikko.yugen.dto.like.PostLikeResponseDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.integration.base.IntegrationTestBase;
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

class PostIntegrationTest extends IntegrationTestBase {

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
        user.setRole(Role.USER);
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
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/posts",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);

        List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");

        Assertions.assertFalse(content.isEmpty());

        Map<String, Object> firstPost = content.get(0);
        Assertions.assertNotNull(firstPost.get("content"));
        Assertions.assertNotNull(firstPost.get("artistId"));
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
        user.setRole(Role.ARTIST);
        userRepository.save(user);

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
        otherUser.setRole(Role.ARTIST);
        userRepository.save(otherUser);

        Artist otherArtist = new Artist();
        otherArtist.setArtistName("other");
        otherArtist.setUser(otherUser);
        artistRepository.save(otherArtist);
        otherUser.setArtist(otherArtist);

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
        user.setRole(Role.ARTIST);
        userRepository.save(user);
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
        otherUser.setRole(Role.ARTIST);
        userRepository.save(otherUser);

        Artist otherArtist = new Artist();
        otherArtist.setArtistName("other");
        otherArtist.setUser(otherUser);
        artistRepository.save(otherArtist);
        otherUser.setArtist(otherArtist);

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
    void likePost_returns200_andPostLikeResponseDTO() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PostLikeResponseDTO> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/likes",
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
    void unlikePost_returns200_andPostLikeResponseDTO() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/likes",
                HttpMethod.POST,
                request,
                PostLikeResponseDTO.class
        );

        ResponseEntity<PostLikeResponseDTO> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/likes",
                HttpMethod.DELETE,
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