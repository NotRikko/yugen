package rikko.yugen.dto.cart;

import rikko.yugen.model.CartItem;
import rikko.yugen.dto.product.ProductDTO;

public class CartItemDTO {
    private final Long id;
    private final Long productId;
    private final ProductDTO product;
    private final int quantity;

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.product = ProductDTO.fromProduct(cartItem.getProduct());
        this.quantity = cartItem.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}