package rikko.yugen.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;

import rikko.yugen.dto.product.ProductUpdateDTO;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") ProductCreateDTO productCreateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        ProductDTO createdProduct = productService.createProduct(productCreateDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping(value = "/{productId}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product")ProductUpdateDTO productUpdateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
            ) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productUpdateDTO, files);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}