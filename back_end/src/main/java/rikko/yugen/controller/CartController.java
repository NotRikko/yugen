package rikko.yugen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rikko.yugen.dto.cart.CartAddItemDTO;
import rikko.yugen.dto.cart.CartDTO;
import rikko.yugen.dto.cart.CartUpdateItemDTO;
import rikko.yugen.dto.cart.CheckoutResponseDTO;
import rikko.yugen.service.CartService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cart = cartService.getOrCreateCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addItem(@Valid @RequestBody CartAddItemDTO dto) {
        CartDTO cart = cartService.addItem(dto);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> updateItem(@Valid @RequestBody CartUpdateItemDTO dto) {
        CartDTO cart = cartService.updateItem(dto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> removeItem(@RequestParam Long cartItemId) {
        CartDTO cart = cartService.removeItem(cartItemId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/clear")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> clearCart() {
        CartDTO cart = cartService.clearCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CheckoutResponseDTO> checkoutCart() {
        CheckoutResponseDTO result = cartService.checkout();
        return ResponseEntity.ok(result);
    }
}