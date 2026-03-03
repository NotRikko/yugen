package rikko.yugen.dto.cart;

import rikko.yugen.dto.product.ProductSummaryDTO;
import rikko.yugen.model.CartItem;

public record CartItemDTO(
        Long id,
        ProductSummaryDTO product,
        int quantity
) {
    public CartItemDTO(CartItem cartItem) {
        this(
                cartItem.getId(),
                cartItem.getProduct() != null ? new ProductSummaryDTO(cartItem.getProduct()) : null,
                cartItem.getQuantity()
        );
    }
}