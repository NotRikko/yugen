package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rikko.yugen.model.Cart;
import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;

import rikko.yugen.dto.cart.CartDTO;

import rikko.yugen.repository.CartRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.UserRepository;

import rikko.yugen.service.CartItemService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;

    private final CurrentUserHelper currentUserHelper;

    @Transactional
    public CartDTO getOrCreateCart() {
        Cart cart = getOrCreateCartEntity();
        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO addItem(Long productId, int quantity) {
        Cart cart = getOrCreateCartEntity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemService.getItemsByCartId(cart.getId()).stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(item.getQuantity() + quantity);
                            cartItemService.save(item);
                        },
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(quantity);
                            cartItemService.save(newItem);

                            cart.getItems().add(newItem);
                        }
                );
        return new CartDTO(cart);
    }

    @Transactional
    public CartDTO updateItem(Long cartItemId, int quantity) {
        if (quantity < 1) throw new RuntimeException("Quantity must be at least 1");

        CartItem item = cartItemService.getItemById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setQuantity(quantity);
        cartItemService.save(item);

        return new CartDTO(item.getCart());
    }

    @Transactional
    public CartDTO removeItem(Long cartItemId) {
        CartItem item = cartItemService.getItemById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = item.getCart();
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

        cartItemService.getItemsByCartId(cart.getId())
                .forEach(item -> cartItemService.delete(item.getId()));

        return "Checkout successful! Your order has been placed.";
    }

    private Cart getOrCreateCartEntity() {
        User currentUser = currentUserHelper.getCurrentUser();
        return cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(currentUser);
                    return cartRepository.save(newCart);
                });
    }
}