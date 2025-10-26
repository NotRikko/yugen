package rikko.yugen.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;

import rikko.yugen.service.ProductService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{productId}/purchase")
    public ResponseEntity<String> purchaseProduct(@PathVariable Long productId) {
        String message = productService.purchaseProduct(productId);
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") ProductCreateDTO productCreateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        ProductDTO createdProduct = productService.createProduct(productCreateDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}