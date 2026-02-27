package rikko.yugen.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rikko.yugen.dto.cart.CartAddItemDTO;
import rikko.yugen.dto.cart.CartUpdateItemDTO;
import rikko.yugen.dto.cart.CheckoutResponseDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;

import rikko.yugen.dto.cart.CartDTO;

import rikko.yugen.repository.CartItemRepository;
import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;

    private final CurrentUserHelper currentUserHelper;
    private final CartItemRepository cartItemRepository;

    // Helpers

    private Cart getOrCreateCartEntity() {
        User currentUser = currentUserHelper.getCurrentUser();
        return cartRepository.findByUserIdWithItems(currentUser.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(currentUser);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public CartDTO getOrCreateCart() {
        Cart cart = getOrCreateCartEntity();
        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO addItem(CartAddItemDTO request) {
        if (request.getQuantity() < 1)
            throw new IllegalArgumentException("Quantity must be at least 1");

        Cart cart = getOrCreateCartEntity();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        if (request.getQuantity() > product.getQuantityInStock()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO updateItem(CartUpdateItemDTO request) {
        if (request.getQuantity() < 1)
            throw new IllegalArgumentException("Quantity must be at least 1");

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", request.getCartItemId()));

        User currentUser = currentUserHelper.getCurrentUser();
        if (!item.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this cart item");
        }

        if (request.getQuantity() > item.getProduct().getQuantityInStock()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return new CartDTO(item.getCart());
    }

    @Transactional
    public CartDTO removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        User currentUser = currentUserHelper.getCurrentUser();
        if (!item.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this cart item");
        }

        Cart cart = item.getCart();
        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO clearCart() {
        Cart cart = getOrCreateCartEntity();

        User currentUser = currentUserHelper.getCurrentUser();

        if (!cart.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this cart");
        }

        cartItemRepository.deleteAllByCartId(cart.getId());
        return new CartDTO(cart);
    }

    @Transactional
    public CheckoutResponseDTO checkout() {
        Cart cart = getOrCreateCartEntity();
        List<CartItem> items = new ArrayList<>(cart.getItems());

        CheckoutResponseDTO response = new CheckoutResponseDTO();

        if (items.isEmpty()) {
            response.setSuccess(false);
            response.getMessages().add("Your cart is empty.");
            return response;
        }

        for (CartItem item : items) {
            Product product = item.getProduct();
            int quantityNeeded = item.getQuantity();
            boolean success = false;
            int retries = 3;

            while (!success && retries > 0) {
                try {
                    Product currentProduct = productRepository.findById(product.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", product.getId()));

                    if (currentProduct.getQuantityInStock() < quantityNeeded) {
                        response.getMessages().add("Not enough stock for product: " +
                                currentProduct.getName() + ". Available: " + currentProduct.getQuantityInStock());
                        break;
                    }

                    currentProduct.setQuantityInStock(currentProduct.getQuantityInStock() - quantityNeeded);
                    productRepository.save(currentProduct);
                    success = true;
                } catch (OptimisticLockException e) {
                    retries--;
                    if (retries == 0) {
                        response.getMessages().add("Could not update stock for product: " +
                                product.getName() + ". Please try again.");
                    }
                }
            }
        }

        if (!response.getMessages().isEmpty()) {
            response.setSuccess(false);
            return response;
        }

        cartItemRepository.deleteAllByCartId(cart.getId());
        cart.getItems().clear();

        response.setSuccess(true);
        response.getMessages().add("Checkout successful! Your order has been placed.");
        return response;
    }

}