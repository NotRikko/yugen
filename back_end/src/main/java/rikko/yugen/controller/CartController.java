package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.model.Cart;
import rikko.yugen.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getOrCreateCart());
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItem(productId, quantity));
    }

    @PatchMapping("/update")
    public ResponseEntity<Cart> updateItem(@RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItem(productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeItem(@RequestParam Long productId) {
        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    @PostMapping("/clear")
    public ResponseEntity<Cart> clearCart() {
        return ResponseEntity.ok(cartService.clearCart());
    }

    @PostMapping("/merge")
    public ResponseEntity<Cart> mergeGuestCart(@RequestBody Cart guestCart) {
        return ResponseEntity.ok(cartService.mergeGuestCart(guestCart));
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart() {
        String result = cartService.checkout();
        return ResponseEntity.ok(result);
    }
}