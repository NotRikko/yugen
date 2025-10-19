package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikko.yugen.dto.cart.CartDTO;
import rikko.yugen.service.CartService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cart = cartService.getOrCreateCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItem(@RequestParam Long productId,
                                           @RequestParam int quantity) {
        CartDTO cart = cartService.addItem(productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/update")
    public ResponseEntity<CartDTO> updateItem(@RequestParam Long cartItemId,
                                              @RequestParam int quantity) {
        CartDTO cart = cartService.updateItem(cartItemId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeItem(@RequestParam Long cartItemId) {
        CartDTO cart = cartService.removeItem(cartItemId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/clear")
    public ResponseEntity<CartDTO> clearCart() {
        CartDTO cart = cartService.clearCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart() {
        String result = cartService.checkout();
        return ResponseEntity.ok(result);
    }
}
