package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.model.Cart;
import rikko.yugen.dto.cart.CartDTO;
import rikko.yugen.service.CartService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        Cart cart = cartService.getOrCreateCart();
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItem(@RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.addItem(productId, quantity);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @PatchMapping("/update")
    public ResponseEntity<CartDTO> updateItem(@RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.updateItem(productId, quantity);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeItem(@RequestParam Long productId) {
        Cart cart = cartService.removeItem(productId);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @PostMapping("/clear")
    public ResponseEntity<CartDTO> clearCart() {
        Cart cart = cartService.clearCart();
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @PostMapping("/merge")
    public ResponseEntity<CartDTO> mergeGuestCart(@RequestBody Cart guestCart) {
        Cart cart = cartService.mergeGuestCart(guestCart);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart() {
        String result = cartService.checkout();
        return ResponseEntity.ok(result);
    }
}