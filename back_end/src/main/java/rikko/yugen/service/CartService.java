package rikko.yugen.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;

import rikko.yugen.dto.cart.CartDTO;

import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;

    private final CurrentUserHelper currentUserHelper;

    // Helpers

    private Cart getOrCreateCartEntity() {
        User currentUser = currentUserHelper.getCurrentUser();
        return cartRepository.findByUserId(currentUser.getId())
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
    public CartDTO addItem(Long productId, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");

        Cart cart = getOrCreateCartEntity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        List<CartItem> items = cartItemService.getItemsByCartId(cart.getId());

        CartItem item = items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemService.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemService.save(newItem);
            cart.getItems().add(newItem);
        }

        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO updateItem(Long cartItemId, int quantity) {

        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");

        CartItem item = cartItemService.getItemById(cartItemId);

        User currentUser = currentUserHelper.getCurrentUser();

        if (!item.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this cart");
        }

        if (quantity > item.getProduct().getQuantityInStock()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        item.setQuantity(quantity);
        cartItemService.save(item);

        return new CartDTO(item.getCart());
    }

    @Transactional
    public CartDTO removeItem(Long cartItemId) {
        CartItem item = cartItemService.getItemById(cartItemId);

        Cart cart = item.getCart();
        cartItemService.delete(item.getId());
        cart.getItems().remove(item);
        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO clearCart() {
        Cart cart = getOrCreateCartEntity();
        cartItemService.getItemsByCartId(cart.getId())
                .forEach(item -> cartItemService.delete(item.getId()));
        return new CartDTO(cart);
    }

    @Transactional
    public String checkout() {
        Cart cart = getOrCreateCartEntity();
        List<CartItem> items = new ArrayList<>(cartItemService.getItemsByCartId(cart.getId()));

        if (items.isEmpty()) return "Your cart is empty.";

        StringBuilder messages = new StringBuilder();

        for (CartItem item : items) {
            Product product = item.getProduct();
            int quantityNeeded = item.getQuantity();
            boolean success = false;
            int retries = 3;

            while (!success && retries > 0) {
                try {
                    Product finalProduct = product;
                    product = productRepository.findById(product.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", finalProduct.getId()));

                    if (product.getQuantityInStock() < quantityNeeded) {
                        messages.append("Not enough stock for product: ")
                                .append(product.getName())
                                .append(". Available: ")
                                .append(product.getQuantityInStock())
                                .append("\n");
                        break;
                    }

                    product.setQuantityInStock(product.getQuantityInStock() - quantityNeeded);
                    productRepository.save(product);
                    success = true;
                } catch (OptimisticLockException e) {
                    retries--;
                    if (retries == 0) {
                        messages.append("Could not update stock for product: ")
                                .append(product.getName())
                                .append(". Please try again.\n");
                    }
                }
            }
        }

        if (!messages.isEmpty()) return "Checkout failed:\n" + messages;

        items.forEach(i -> cartItemService.delete(i.getId()));

        return "Checkout successful! Your order has been placed.";
    }

}