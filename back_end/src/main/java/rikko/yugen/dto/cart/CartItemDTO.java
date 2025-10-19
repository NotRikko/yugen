package rikko.yugen.dto.cart;

import rikko.yugen.model.CartItem;
import rikko.yugen.model.Product;
import rikko.yugen.dto.product.ProductDTO;

public class CartItemDTO {
    private final Long id;  // NEW
    private final Long productId;
    private final String productName;
    private final int quantity;

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();  // map the CartItem's ID
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.quantity = cartItem.getQuantity();
    }

    // Getters
    public Long getCartId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}