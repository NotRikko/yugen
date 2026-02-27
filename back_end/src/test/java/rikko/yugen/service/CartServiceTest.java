package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.cart.CartAddItemDTO;
import rikko.yugen.dto.cart.CartDTO;
import rikko.yugen.dto.cart.CartUpdateItemDTO;
import rikko.yugen.dto.cart.CheckoutResponseDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.CartItemRepository;
import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;

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
    private CartItemRepository cartItemRepository;

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
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));

            CartDTO result = cartService.getOrCreateCart();

            assertEquals(mockCart.getId(), result.id());
        }

        @Test
        void getOrCreateCart_shouldCreateNewCartIfNotExist() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L)).thenReturn(Optional.empty());
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
            CartAddItemDTO request = new CartAddItemDTO(100L, 3);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));
            when(productRepository.findById(100L))
                    .thenReturn(Optional.of(mockProduct));
            when(cartItemRepository.findByCartIdAndProductId(10L, 100L))
                    .thenReturn(Optional.empty());
            when(cartItemRepository.save(any(CartItem.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CartDTO result = cartService.addItem(request);

            assertEquals(1, result.items().size());
            assertEquals(3, result.items().get(0).quantity());
        }

        @Test
        void addItem_shouldIncrementQuantityIfExists() {
            CartAddItemDTO request = new CartAddItemDTO(100L, 2);

            mockItem.setQuantity(2);
            mockCart.getItems().add(mockItem);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));
            when(productRepository.findById(100L))
                    .thenReturn(Optional.of(mockProduct));
            when(cartItemRepository.findByCartIdAndProductId(10L, 100L))
                    .thenReturn(Optional.of(mockItem));
            when(cartItemRepository.save(any(CartItem.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CartDTO result = cartService.addItem(request);

            assertEquals(1, result.items().size());
            assertEquals(4, result.items().get(0).quantity());
        }

        @Test
        void addItem_shouldThrow_whenQuantityLessThanOne() {
            CartAddItemDTO request = new CartAddItemDTO(100L, 0);

            assertThrows(IllegalArgumentException.class,
                    () -> cartService.addItem(request));
        }
    }

    // Update item tests

    @Nested
    class UpdateItemTests {

        @Test
        void updateItem_shouldUpdateQuantity() {
            CartUpdateItemDTO request = new CartUpdateItemDTO(1L, 5);

            mockItem.setCart(mockCart);
            mockCart.getItems().add(mockItem);

            when(cartItemRepository.findById(1L))
                    .thenReturn(Optional.of(mockItem));
            when(currentUserHelper.getCurrentUser())
                    .thenReturn(mockUser);
            when(cartItemRepository.save(mockItem))
                    .thenReturn(mockItem);

            CartDTO result = cartService.updateItem(request);

            assertEquals(5, result.items().get(0).quantity());
        }

        @Test
        void updateItem_shouldThrow_whenQuantityInvalid() {
            CartUpdateItemDTO request = new CartUpdateItemDTO(1L, 0);

            assertThrows(IllegalArgumentException.class,
                    () -> cartService.updateItem(request));
        }
    }

    // Remove item tests
    @Nested
    class RemoveItemTests {
        @Test
        void removeItem_shouldDeleteItemFromCart() {
            mockCart.getItems().add(mockItem);
            when(cartItemRepository.findById(1L))
                    .thenReturn(Optional.of(mockItem));
            when(currentUserHelper.getCurrentUser())
                    .thenReturn(mockUser);

            CartDTO result = cartService.removeItem(1L);

            verify(cartItemRepository).delete(mockItem);
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
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));

            CartDTO result = cartService.clearCart();

            verify(cartItemRepository).deleteAllByCartId(mockCart.getId());
            assertTrue(result.items().isEmpty());
        }
    }

    // Checkout tests

    @Nested
    class CheckoutTests {
        @Test
        void checkout_shouldReturnFailure_whenCartIsEmpty() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));

            CheckoutResponseDTO result = cartService.checkout();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessages().contains("Your cart is empty."));
            verify(cartItemRepository, never()).deleteAllByCartId(anyLong());
        }

        @Test
        void checkout_shouldReduceStockAndClearCart() {
            mockCart.getItems().add(mockItem);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));
            when(productRepository.findById(100L))
                    .thenReturn(Optional.of(mockProduct));

            when(productRepository.save(any(Product.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            CheckoutResponseDTO result = cartService.checkout();

            assertTrue(result.isSuccess());
            assertTrue(result.getMessages()
                    .contains("Checkout successful! Your order has been placed."));

            assertEquals(3, mockProduct.getQuantityInStock());

            verify(cartItemRepository).deleteAllByCartId(mockCart.getId());
            assertTrue(mockCart.getItems().isEmpty());
        }

        @Test
        void checkout_shouldFailWhenNotEnoughStock() {
            mockItem.setQuantity(10);
            mockCart.getItems().add(mockItem);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(cartRepository.findByUserIdWithItems(1L))
                    .thenReturn(Optional.of(mockCart));

            when(productRepository.findById(100L))
                    .thenReturn(Optional.of(mockProduct));

            CheckoutResponseDTO result = cartService.checkout();

            assertFalse(result.isSuccess());
            assertTrue(result.getMessages().get(0)
                    .contains("Not enough stock"));

            assertEquals(5, mockProduct.getQuantityInStock());

            verify(cartItemRepository, never()).deleteAllByCartId(anyLong());
        }
    }
}