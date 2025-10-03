package rikko.yugen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import rikko.yugen.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{productId}/purchase")
    public ResponseEntity<String> purchaseProduct(@PathVariable Long productId) {
        String message = productService.purchaseProduct(productId);
        return ResponseEntity.ok(message);
    }

}