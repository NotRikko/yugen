package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentUpdateDTO;
import rikko.yugen.dto.like.CommentLikeResponseDTO;
import rikko.yugen.integration.base.IntegrationTestBase;
import rikko.yugen.model.*;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class CommentIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtService jwtService;

    private User user;
    private Artist artist;
    private Post savedPost;
    private Comment savedComment;

    @BeforeEach
    void cleanUpAndSetUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("rikko3");
        user.setPassword("test12345!");
        userRepository.save(user);

        artist = new Artist();
        artist.setArtistName("testArtist");
        artist.setUser(user);
        artistRepository.save(artist);

        Post post = new Post();
        post.setArtist(artist);
        post.setContent("Existing post");
        post.setCreatedAt(LocalDateTime.now());
        savedPost = postRepository.save(post);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(savedPost);
        comment.setContent("Existing comment");
        savedComment = commentRepository.save(comment);
    }

    private CommentCreateDTO validCommentDto() {
        CommentCreateDTO dto = new CommentCreateDTO();
        dto.setPostId(savedPost.getId());
        dto.setContent("This is a test comment");
        return dto;
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

    // Create tests

    @Test
    void createComment_returns201_whenUserIsAuthenticated() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CommentCreateDTO> request = new HttpEntity<>(validCommentDto(), headers);

        ResponseEntity<CommentDTO> response = restTemplate.postForEntity("/comments", request, CommentDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CommentDTO body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("This is a test comment", body.content());
        Assertions.assertEquals(user.getId(), body.user().id());
    }

    // Update tests

    @Test
    void updateComment_returns200_whenUserIsOwner() {
        String token = getToken(user, Role.USER);

        CommentUpdateDTO updateDto = new CommentUpdateDTO();
        updateDto.setContent("Updated comment");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CommentUpdateDTO> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<CommentDTO> response = restTemplate.exchange(
                "/comments/" + savedComment.getId(),
                HttpMethod.PUT,
                request,
                CommentDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated comment", response.getBody().content());
    }

    @Test
    void updateComment_returns403_whenUserIsNotOwner() {
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("pass123");
        userRepository.save(otherUser);

        String token = getToken(otherUser, Role.USER);

        CommentUpdateDTO updateDto = new CommentUpdateDTO();
        updateDto.setContent("Hacked comment");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CommentUpdateDTO> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/comments/" + savedComment.getId(),
                HttpMethod.PUT,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Delete tests

    @Test
    void deleteComment_returns204_whenUserIsOwner() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/comments/" + savedComment.getId(),
                HttpMethod.DELETE,
                request,
                Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertFalse(commentRepository.findById(savedComment.getId()).isPresent());
    }

    @Test
    void deleteComment_returns403_whenUserIsNotOwner() {
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("pass123");
        userRepository.save(otherUser);

        String token = getToken(otherUser, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/comments/" + savedComment.getId(),
                HttpMethod.DELETE,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Get tests

    @Test
    void getCommentsByPost_returns200_andComments() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/posts/" + savedPost.getId() + "/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);

        List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
        Assertions.assertFalse(content.isEmpty());
        Assertions.assertEquals(savedComment.getContent(), content.get(0).get("content"));
    }

    /* Move to user integration test

    @Test
    void getCommentsByUser_returns200_andComments() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/comments/user/" + user.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);

        List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
        Assertions.assertFalse(content.isEmpty());
        Assertions.assertEquals(savedComment.getContent(), content.get(0).get("content"));
    }

    */


    // Toggle like test

    @Test
    void toggleLike_returns200_andCommentLikeResponseDTO() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CommentLikeResponseDTO> response = restTemplate.exchange(
                "/comments/" + savedComment.getId() + "/like",
                HttpMethod.POST,
                request,
                CommentLikeResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentLikeResponseDTO body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.likedByCurrentUser());
        Assertions.assertEquals(1, body.likes());
    }

    @Test
    void toggleLike_returns200_andCommentLikeResponseDTO_unlike() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                "/comments/" + savedComment.getId() + "/like",
                HttpMethod.POST,
                request,
                CommentLikeResponseDTO.class
        );

        ResponseEntity<CommentLikeResponseDTO> response = restTemplate.exchange(
                "/comments/" + savedComment.getId() + "/like",
                HttpMethod.POST,
                request,
                CommentLikeResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentLikeResponseDTO body = response.getBody();
        Assertions.assertNotNull(body);

        Assertions.assertFalse(body.likedByCurrentUser());
        Assertions.assertEquals(0, body.likes());
    }
}