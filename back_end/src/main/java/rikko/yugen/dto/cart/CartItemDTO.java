package rikko.yugen.dto.cart;

import rikko.yugen.model.CartItem;
import rikko.yugen.dto.product.ProductDTO;

public record CartItemDTO(
        Long id,
        Long productId,
        String productName,
        Float productPrice,
        int quantity
) {
    public CartItemDTO(CartItem cartItem) {
        this(
                cartItem.getId(),
                cartItem.getProduct() != null ? cartItem.getProduct().getId() : null,
                cartItem.getProduct() != null ? cartItem.getProduct().getName() : null,
                cartItem.getProduct() != null ? cartItem.getProduct().getPrice() : null,
                cartItem.getQuantity()
        );
    }
}