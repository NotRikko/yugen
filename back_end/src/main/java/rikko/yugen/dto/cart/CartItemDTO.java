package rikko.yugen.dto.cart;

import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.dto.product.ProductDTO;

public class CartItemDTO {
    private final Long id;
    private final ProductDTO product;
    private final int quantity;

    public CartItemDTO(CartItem item) {
        this.id = item.getId();
        this.product = item.getProduct() != null ? new ProductDTO(item.getProduct()) : null;
        this.quantity = item.getQuantity();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}