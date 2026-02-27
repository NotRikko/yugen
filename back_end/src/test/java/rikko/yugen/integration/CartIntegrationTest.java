package rikko.yugen.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rikko.yugen.dto.cart.*;
import rikko.yugen.integration.base.IntegrationTestBase;
import rikko.yugen.model.*;
import rikko.yugen.repository.*;
import rikko.yugen.service.JwtService;

class CartIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    private User user;
    private User otherUser;
    private Product product;

    @BeforeEach
    void cleanUpAndSetUp() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();

        // Main user
        user = new User();
        user.setUsername("rikko");
        user.setPassword("test123!");
        user.setRole(Role.USER);
        userRepository.save(user);

        // Another user for ownership tests
        otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("test123!");
        otherUser.setRole(Role.USER);
        userRepository.save(otherUser);

        Artist artist = new Artist();
        artist.setArtistName("test");
        artist.setUser(user);
        artistRepository.save(artist);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(10F);
        product.setDescription("Test description");
        product.setArtist(artist);
        product.setQuantityInStock(10);
        productRepository.save(product);
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

    private CartDTO addItem(User user, Product product, int quantity) {
        CartAddItemDTO dto = new CartAddItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(quantity);
        String token = getToken(user, Role.USER);
        HttpEntity<CartAddItemDTO> request = new HttpEntity<>(dto, createJsonHeaders(token));
        return restTemplate.postForEntity("/cart/add", request, CartDTO.class).getBody();
    }

    private CartDTO getCart(User user) {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));
        return restTemplate.exchange("/cart", HttpMethod.GET, request, CartDTO.class).getBody();
    }

    private ResponseEntity<CartDTO> updateItem(User user, Long cartItemId, int quantity) {
        CartUpdateItemDTO dto = new CartUpdateItemDTO();
        dto.setCartItemId(cartItemId);
        dto.setQuantity(quantity);
        String token = getToken(user, Role.USER);
        HttpEntity<CartUpdateItemDTO> request = new HttpEntity<>(dto, createJsonHeaders(token));
        return restTemplate.exchange("/cart/update", HttpMethod.PATCH, request, CartDTO.class);
    }

    private ResponseEntity<CartDTO> removeItem(User user, Long cartItemId) {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));
        return restTemplate.exchange("/cart/remove?cartItemId=" + cartItemId, HttpMethod.DELETE, request, CartDTO.class);
    }

    private ResponseEntity<CartDTO> clearCart(User user) {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));
        return restTemplate.postForEntity("/cart/clear", request, CartDTO.class);
    }

    private ResponseEntity<CheckoutResponseDTO> checkout(User user) {
        String token = getToken(user, Role.USER);
        HttpEntity<Void> request = new HttpEntity<>(createJsonHeaders(token));
        return restTemplate.postForEntity("/cart/checkout", request, CheckoutResponseDTO.class);
    }

    // Get tests
    @Test
    void getCart_returns200_andEmptyCart() {
        CartDTO cart = getCart(user);
        Assertions.assertNotNull(cart);
        Assertions.assertTrue(cart.items().isEmpty());
    }

    // Add tests
    @Test
    void addItem_returns200_andAddsItem() {
        CartDTO cart = addItem(user, product, 2);
        Assertions.assertEquals(1, cart.items().size());
        CartItemDTO item = cart.items().get(0);
        Assertions.assertEquals(product.getId(), item.productId());
        Assertions.assertEquals(2, item.quantity());
    }

    @Test
    void addItem_throwsForQuantityLessThan1() {
        CartAddItemDTO dto = new CartAddItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(0);
        String token = getToken(user, Role.USER);

        HttpEntity<CartAddItemDTO> request = new HttpEntity<>(dto, createJsonHeaders(token));
        ResponseEntity<String> response = restTemplate.postForEntity("/cart/add", request, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addItem_throwsForQuantityExceedingStock() {
        CartAddItemDTO dto = new CartAddItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(100);
        String token = getToken(user, Role.USER);

        HttpEntity<CartAddItemDTO> request = new HttpEntity<>(dto, createJsonHeaders(token));
        ResponseEntity<String> response = restTemplate.postForEntity("/cart/add", request, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Update tests
    @Test
    void updateItem_returns200_andUpdatesQuantity() {
        CartDTO cart = addItem(user, product, 1);
        Long cartItemId = cart.items().get(0).id();

        ResponseEntity<CartDTO> response = updateItem(user, cartItemId, 5);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(5, response.getBody().items().get(0).quantity());
    }

    @Test
    void cannotUpdateOrRemoveOtherUsersCartItems() {
        CartDTO cart = addItem(user, product, 1);
        Long cartItemId = cart.items().get(0).id();

        ResponseEntity<CartDTO> updateResponse = updateItem(otherUser, cartItemId, 5);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, updateResponse.getStatusCode());

        ResponseEntity<CartDTO> removeResponse = removeItem(otherUser, cartItemId);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, removeResponse.getStatusCode());
    }

    // Remove tests
    @Test
    void removeItem_returns200_andRemovesItem() {
        CartDTO cart = addItem(user, product, 1);
        Long cartItemId = cart.items().get(0).id();

        ResponseEntity<CartDTO> response = removeItem(user, cartItemId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().items().isEmpty());
    }

    // Clear cart tests
    @Test
    void clearCart_returns200_andEmptiesCart() {
        addItem(user, product, 1);
        addItem(user, product, 1);

        ResponseEntity<CartDTO> response = clearCart(user);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().items().isEmpty());
    }

    // ===== Checkout Tests =====
    @Test
    void checkoutCart_returns200_andClearsCart() {
        addItem(user, product, 2);

        ResponseEntity<CheckoutResponseDTO> response = checkout(user);
        CheckoutResponseDTO checkout = response.getBody();
        Assertions.assertNotNull(checkout);
        Assertions.assertTrue(checkout.isSuccess());
        Assertions.assertTrue(checkout.getMessages().contains("Checkout successful! Your order has been placed."));

        CartDTO cartAfter = getCart(user);
        Assertions.assertTrue(cartAfter.items().isEmpty());
    }

    @Test
    void checkoutFails_whenCartEmpty() {
        ResponseEntity<CheckoutResponseDTO> response = checkout(user);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isSuccess());
        Assertions.assertTrue(response.getBody().getMessages().contains("Your cart is empty."));
    }
}
