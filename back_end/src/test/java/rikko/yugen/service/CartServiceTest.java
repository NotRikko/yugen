package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.cart.CartDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private CartService cartService;

    private User mockUser;
    private Cart mockCart;
    private Product mockProduct;
    private CartItem mockItem;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockCart = new Cart();
        mockCart.setId(10L);
        mockCart.setUser(mockUser);

        mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setName("Test Product");
        mockProduct.setQuantityInStock(5);

        mockItem = new CartItem();
        mockItem.setId(1L);
        mockItem.setCart(mockCart);
        mockItem.setProduct(mockProduct);
        mockItem.setQuantity(2);
    }

    // Get/Create cart tests

    @Nested
    class GetOrCreateCartTests {
        @Test
        void getOrCreateCart_shouldReturnExistingCart() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));

            CartDTO result = cartService.getOrCreateCart();

            assertEquals(mockCart.getId(), result.id());
        }

        @Test
        void getOrCreateCart_shouldCreateNewCartIfNotExist() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
            when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

            CartDTO result = cartService.getOrCreateCart();

            assertEquals(mockUser.getId(), result.userId());
        }
    }

    // Add item tests

    @Nested
    class AddItemTests {
        @Test
        void addItem_shouldAddNewItemToCart() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(List.of());
            when(cartItemService.save(any(CartItem.class))).thenAnswer(i -> i.getArguments()[0]);

            CartDTO result = cartService.addItem(100L, 3);

            assertEquals(1, result.items().size());
            assertEquals(3, result.items().get(0).quantity());
        }

        @Test
        void addItem_shouldIncrementQuantityIfExists() {
            mockCart.getItems().add(mockItem);
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(List.of(mockItem));
            when(cartItemService.save(any(CartItem.class))).thenAnswer(i -> i.getArguments()[0]);

            CartDTO result = cartService.addItem(100L, 2);

            assertEquals(1, result.items().size());
            assertEquals(4, result.items().get(0).quantity());
        }

        @Test
        void addItem_shouldThrow_whenQuantityLessThanOne() {
            assertThrows(IllegalArgumentException.class, () -> cartService.addItem(100L, 0));
        }
    }

    // Update item tests

    @Nested
    class UpdateItemTests {
        @Test
        void updateItem_shouldUpdateQuantity() {
            mockCart.getItems().add(mockItem);
            when(cartItemService.getItemById(1L)).thenReturn(mockItem);
            when(cartItemService.save(mockItem)).thenReturn(mockItem);
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

            CartDTO result = cartService.updateItem(1L, 5);

            assertEquals(5, result.items().get(0).quantity());
        }

        @Test
        void updateItem_shouldThrow_whenQuantityInvalid() {
            assertThrows(IllegalArgumentException.class, () -> cartService.updateItem(1L, 0));
        }
    }

    // Remove item tests
    @Nested
    class RemoveItemTests {
        @Test
        void removeItem_shouldDeleteItemFromCart() {
            when(cartItemService.getItemById(1L)).thenReturn(mockItem);

            CartDTO result = cartService.removeItem(1L);

            verify(cartItemService, times(1)).delete(1L);
            assertTrue(result.items().isEmpty());
        }
    }

    // Clear cart tests

    @Nested
    class ClearCartTests {
        @Test
        void clearCart_shouldDeleteAllItems() {
            mockCart.getItems().add(mockItem);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(new ArrayList<>(mockCart.getItems()));

            doAnswer(invocation -> {
                mockCart.getItems().clear();
                return null;
            }).when(cartItemService).delete(anyLong());

            CartDTO result = cartService.clearCart();

            verify(cartItemService, times(1)).delete(mockItem.getId());
            assertTrue(result.items().isEmpty());
        }
    }

    // Checkout tests

    @Nested
    class CheckoutTests {
        @Test
        void checkout_shouldReturnEmptyCartMessage() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(List.of());

            String result = cartService.checkout();

            assertEquals("Your cart is empty.", result);
        }

        @Test
        void checkout_shouldReduceStockAndDeleteItems() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(List.of(mockItem));
            when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));
            when(productRepository.save(mockProduct)).thenReturn(mockProduct);

            String result = cartService.checkout();

            assertEquals("Checkout successful! Your order has been placed.", result);
            assertEquals(3, mockProduct.getQuantityInStock()); // 5 - 2
            verify(cartItemService).delete(mockItem.getId());
        }

        @Test
        void checkout_shouldFailWhenNotEnoughStock() {
            mockItem.setQuantity(10); // more than stock
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(mockCart));
            when(cartItemService.getItemsByCartId(10L)).thenReturn(List.of(mockItem));
            when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));

            String result = cartService.checkout();

            assertTrue(result.contains("Not enough stock"));
            verify(cartItemService, never()).delete(any());
        }
    }
}