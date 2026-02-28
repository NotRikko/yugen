package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import rikko.yugen.dto.PageResponseDTO;
import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.integration.base.IntegrationTestBase;
import rikko.yugen.model.*;
import rikko.yugen.repository.*;
import rikko.yugen.service.JwtService;

class ArtistIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private JwtService jwtService;

    private User user;
    private User otherUser;
    private Artist artist;
    private Product product;
    private Post post;

    @BeforeEach
    void cleanUpAndSetUp() {
        followRepository.deleteAll();
        postRepository.deleteAll();
        productRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        // Main user
        user = new User();
        user.setUsername("rikko");
        user.setPassword("test123!");
        user.setRole(Role.USER);

        otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("test123!");
        otherUser.setRole(Role.USER);

        artist = new Artist();
        artist.setArtistName("TestArtist");
        artist.setUser(user);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(10F);
        product.setDescription("Test description");
        product.setArtist(artist);
        product.setQuantityInStock(10);

        post = new Post();
        post.setContent("Test post");
        post.setArtist(artist);

        user = userRepository.save(user);
        otherUser = userRepository.save(otherUser);
        artist = artistRepository.save(artist);
        product = productRepository.save(product);
        post = postRepository.save(post);
    }

    // ===== Helpers =====
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

    private ResponseEntity<ArtistDTO> createArtist(ArtistCreateDTO dto, User user) {
        String token = getToken(user, Role.USER);
        HttpHeaders headers = createJsonHeaders(token);
        HttpEntity<ArtistCreateDTO> request = new HttpEntity<>(dto, headers);
        return restTemplate.postForEntity("/artists", request, ArtistDTO.class);
    }

    // Get tests

    @Test
    void getAllArtists_returns200_andArtists() {
        String token = getToken(user, Role.USER);
        HttpHeaders headers = createJsonHeaders(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PageResponseDTO<ArtistDTO>> response =
                restTemplate.exchange(
                        "/artists",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PageResponseDTO<ArtistDTO> body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.content().isEmpty());

        ArtistDTO first = body.content().get(0);
        Assertions.assertNotNull(first.id());
        Assertions.assertNotNull(first.artistName());
    }

    @Test
    void getArtistById_returns200_whenExists() {
        ResponseEntity<ArtistDTO> response = restTemplate.getForEntity("/artists/" + artist.getId(), ArtistDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(artist.getArtistName(), response.getBody().artistName());
    }

    @Test
    void getArtistByName_returns200_whenExists() {
        ResponseEntity<ArtistDTO> response = restTemplate.getForEntity("/artists/by-name/" + artist.getArtistName(), ArtistDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(artist.getId(), response.getBody().id());
    }

    // create tests

    @Test
    void createArtist_returns201_andArtist() {
        User testUser = new User();
        testUser.setUsername("newTestUser");
        testUser.setPassword("test123!");
        testUser.setRole(Role.USER);
        testUser = userRepository.save(testUser);

        ArtistCreateDTO dto = new ArtistCreateDTO();
        dto.setArtistName("NewArtist");
        dto.setProfilePictureUrl("https://example.com/profile.jpg");

        ResponseEntity<ArtistDTO> response = createArtist(dto, testUser);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ArtistDTO body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(dto.getArtistName(), body.artistName());
        Assertions.assertEquals(dto.getProfilePictureUrl(), body.profilePictureUrl());

        Assertions.assertEquals(testUser.getId(), body.userId());
    }

    @Test
    void createArtist_throwsWhenDuplicateName() {
        ArtistCreateDTO dto = new ArtistCreateDTO();
        dto.setArtistName(artist.getArtistName());

        String token = getToken(user, Role.USER);
        HttpEntity<ArtistCreateDTO> request = new HttpEntity<>(dto, createJsonHeaders(token));
        ResponseEntity<String> response = restTemplate.postForEntity("/artists", request, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    // Get posts tests

    @Test
    void getPostsByArtistId_returns200_andPosts() {
        String url = "/artists/" + artist.getId() + "/posts";

        ResponseEntity<PageResponseDTO<PostDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PageResponseDTO<PostDTO> body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.content().isEmpty());
        Assertions.assertEquals(post.getContent(), body.content().get(0).content());
    }

    @Test
    void getProductsByArtistId_returns200_andProducts() {
        String url = "/artists/" + artist.getId() + "/products";

        ResponseEntity<PageResponseDTO<ProductDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PageResponseDTO<ProductDTO> body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.content().isEmpty());
        Assertions.assertEquals(product.getName(), body.content().get(0).name());
    }

    // Follower tests
    @Test
    void followAndUnfollowArtist_worksCorrectly() {
        String token = getToken(otherUser, Role.USER);
        System.out.println("Token for otherUser: " + token);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));
        ResponseEntity<FollowWithUserDTO> followResponse = restTemplate.postForEntity("/artists/" + artist.getId() + "/followers", request, FollowWithUserDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, followResponse.getStatusCode());
        Assertions.assertNotNull(followResponse.getBody());
        Assertions.assertEquals(otherUser.getId(), followResponse.getBody().userId());

        ResponseEntity<Boolean> isFollowing = restTemplate.exchange("/artists/" + artist.getId() + "/followers/me", HttpMethod.GET, request, Boolean.class);
        Assertions.assertNotNull(isFollowing.getBody());
        Assertions.assertTrue(isFollowing.getBody());

        ResponseEntity<Void> unfollowResponse = restTemplate.exchange("/artists/" + artist.getId() + "/followers", HttpMethod.DELETE, request, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, unfollowResponse.getStatusCode());

        isFollowing = restTemplate.exchange("/artists/" + artist.getId() + "/followers/me", HttpMethod.GET, request, Boolean.class);
        Assertions.assertNotEquals(Boolean.TRUE, isFollowing.getBody());
    }

    @Test
    void getFollowers_returnsList() {
        Follow follow = new Follow();
        follow.setFollower(otherUser);
        follow.setFollowee(artist);

        FollowId followId = new FollowId();
        followId.setFollowerId(otherUser.getId());
        followId.setFolloweeId(artist.getId());
        follow.setId(followId);

        followRepository.save(follow);

        ResponseEntity<FollowWithUserDTO[]> response =
                restTemplate.getForEntity(
                        "/artists/" + artist.getId() + "/followers",
                        FollowWithUserDTO[].class
                );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(otherUser.getId(), response.getBody()[0].userId());
    }
}