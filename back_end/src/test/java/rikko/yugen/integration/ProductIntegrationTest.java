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
import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.product.ProductUpdateDTO;
import rikko.yugen.model.*;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class ProductIntegrationTest extends IntegrationTestBase {
    
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
    
    @Autowired
    private ProductRepository productRepository;
    
    private User user;
    private Artist artist;
    private Post post;
    private Product product;
    
    @BeforeEach
    void cleanUpandSetUp() {
        productRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();

        user = new User();
        user.setUsername("rikko3");
        user.setPassword("test12345!");
        user.setRole(Role.USER);
        userRepository.save(user);

        artist = new Artist();
        artist.setArtistName("test");
        artist.setUser(user);
        artistRepository.save(artist);

        post = new Post();
        post.setArtist(artist);
        post.setContent("Existing post");
        post.setCreatedAt(LocalDateTime.now());
        post = postRepository.save(post);
        
        product = new Product();
        product.setArtist(artist);
        product.setPrice(11F);
        product.setName("test product");
        product.setDescription("test description");
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    private ProductCreateDTO validDto() {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("test product2");
        dto.setPrice(11F);
        dto.setDescription("test product");
        return dto;
    }

    private ProductUpdateDTO validUpdateDto() {
        ProductUpdateDTO dto = new ProductUpdateDTO();
        dto.setName("updated product");
        dto.setPrice(15F);
        dto.setDescription("updated description");
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

    // Create tests

    @Test
    void createProduct_returns201_whenUserIsArtist() {
        ProductCreateDTO dto = validDto();

        user.setRole(Role.ARTIST);
        userRepository.save(user);


        String token = getToken(user, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new HttpEntity<>(dto, createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<ProductDTO> response =
                restTemplate.postForEntity("/products", request, ProductDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ProductDTO product = response.getBody();
        Assertions.assertNotNull(product);
        Assertions.assertEquals(dto.getDescription(),  product.description());
        Assertions.assertEquals(artist.getId(), product.artistId());
    }

    @Test
    void createProduct_returns201_whenUserIsNotArtist() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new HttpEntity<>(validDto(), createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/products", request, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Get tests

    @Test
    void getAllProducts_returns200_andProducts() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/products",
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

        Map<String, Object> firstProduct = content.get(0);
        Assertions.assertNotNull(firstProduct.get("name"));
        Assertions.assertNotNull(firstProduct.get("price"));
        Assertions.assertNotNull(firstProduct.get("description"));
        Assertions.assertNotNull(firstProduct.get("artistId"));
    }

    // Purchase tests

    @Test
    void purchaseProduct_returns200_whenExists() {
        String token = getToken(user, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/products/" + product.getId() + "/purchase", request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("purchased"));
    }

    // Update tests

    @Test
    void updateProduct_returns200_whenUserIsOwner() {
        user.setRole(Role.ARTIST);
        userRepository.save(user);

        String token = getToken(user, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new HttpEntity<>(validUpdateDto(), createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<ProductDTO> response =
                restTemplate.exchange("/products/" + product.getId(),
                        HttpMethod.PUT,
                        request,
                        ProductDTO.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductDTO updated = response.getBody();
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("updated product", updated.name());
    }

    @Test
    void updateProduct_returns403_whenUserIsNotOwner() {
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
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("product", new HttpEntity<>(validUpdateDto(), createJsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange("/products/" + product.getId(),
                        HttpMethod.PUT,
                        request,
                        String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Delete tests

    @Test
    void deleteProduct_returns204_whenUserIsOwner() {
        user.setRole(Role.ARTIST);
        userRepository.save(user);
        String token = getToken(user, Role.ARTIST);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response =
                restTemplate.exchange("/products/" + product.getId(),
                        HttpMethod.DELETE,
                        request,
                        Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertFalse(productRepository.findById(product.getId()).isPresent());
    }

    @Test
    void deleteProduct_returns403_whenUserIsNotAOwner() {
        User otherUser = new User();
        otherUser.setUsername("other");
        otherUser.setPassword("pass");
        userRepository.save(otherUser);

        Artist otherArtist = new Artist();
        otherArtist.setArtistName("other");
        otherArtist.setUser(otherUser);
        artistRepository.save(otherArtist);
        otherUser.setArtist(otherArtist);


        String token = getToken(otherUser, Role.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response =
                restTemplate.exchange("/products/" + product.getId(),
                        HttpMethod.DELETE,
                        request,
                        Void.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}

