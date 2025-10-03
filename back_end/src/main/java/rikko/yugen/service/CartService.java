package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new RuntimeException("User not authenticated");
    }

    @Transactional
    public Cart getOrCreateCart() {
        User user = getCurrentUser();
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(Long productId, int quantity) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItem(Long productId, int quantity) {
        if (quantity < 1) throw new RuntimeException("Quantity must be at least 1");

        Cart cart = getOrCreateCart();

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(quantity);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(Long productId) {
        Cart cart = getOrCreateCart();
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart mergeGuestCart(Cart guestCart) {
        Cart userCart = getOrCreateCart();
        for (CartItem guestItem : guestCart.getItems()) {
            addItem(guestItem.getProduct().getId(), guestItem.getQuantity());
        }
        return userCart;
    }

    @Transactional
    public String checkout() {
        Cart cart = getOrCreateCart();

        if (cart.getItems().isEmpty()) {
            return "Your cart is empty.";
        }

        StringBuilder messages = new StringBuilder();

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getQuantityInStock() < quantity) {
                messages.append("Not enough stock for product: ")
                        .append(product.getName())
                        .append(". Available: ")
                        .append(product.getQuantityInStock())
                        .append("\n");
                continue;
            }

            product.setQuantityInStock(product.getQuantityInStock() - quantity);
            productRepository.save(product);
        }

        if (messages.length() > 0) {
            return "Checkout failed:\n" + messages.toString();
        }

        cart.getItems().clear();
        cartRepository.save(cart);

        return "Checkout successful! Your order has been placed.";
    }
}