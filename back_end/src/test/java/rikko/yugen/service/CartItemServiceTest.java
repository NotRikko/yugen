package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private CartItem mockItem;

    @BeforeEach
    void setUp() {
        mockItem = new CartItem();
        mockItem.setId(1L);

        Cart mockCart = new Cart();
        mockCart.setId(10L);
        mockItem.setCart(mockCart);
    }

    // Get tests

    @Nested
    class GetCartItemTests {

        @Test
        void getItemsByCartId_shouldReturnList() {
            when(cartItemRepository.findByCart_Id(10L)).thenReturn(List.of(mockItem));

            List<CartItem> result = cartItemService.getItemsByCartId(10L);

            assertEquals(1, result.size());
            assertEquals(mockItem.getId(), result.get(0).getId());
        }

        @Test
        void getItemByIdOrThrow_shouldReturnItem_whenExists() {
            when(cartItemRepository.findById(1L)).thenReturn(Optional.of(mockItem));

            CartItem result = cartItemService.getItemById(1L);

            assertEquals(mockItem.getId(), result.getId());
        }

        @Test
        void getItemByIdOrThrow_shouldThrow_whenNotFound() {
            when(cartItemRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> cartItemService.getItemById(99L));
        }
    }

    // Save tests

    @Nested
    class SaveCartItemTests {

        @Test
        void save_shouldPersistAndReturnItem() {
            when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArguments()[0]);

            CartItem result = cartItemService.save(mockItem);

            assertEquals(mockItem.getId(), result.getId());
            verify(cartItemRepository, times(1)).save(mockItem);
        }

        @Test
        void save_shouldThrow_whenItemIsNull() {
            assertThrows(IllegalArgumentException.class, () -> cartItemService.save(null));
        }
    }

    // Delete tests

    @Nested
    class DeleteCartItemTests {

        @Test
        void delete_shouldDeleteItem_whenExists() {
            when(cartItemRepository.existsById(1L)).thenReturn(true);

            cartItemService.delete(1L);

            verify(cartItemRepository, times(1)).deleteById(1L);
        }

        @Test
        void delete_shouldThrow_whenItemNotFound() {
            when(cartItemRepository.existsById(99L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> cartItemService.delete(99L));
            verify(cartItemRepository, never()).deleteById(any());
        }

        @Test
        void delete_shouldThrow_whenIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> cartItemService.delete(null));
            verify(cartItemRepository, never()).deleteById(any());
        }
    }
}