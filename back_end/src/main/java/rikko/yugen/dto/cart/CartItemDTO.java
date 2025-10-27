package rikko.yugen.dto.cart;

import rikko.yugen.model.CartItem;
import rikko.yugen.dto.product.ProductDTO;

public record CartItemDTO(
        Long id,
        Long productId,
        ProductDTO product,
        int quantity
) {
    public CartItemDTO(CartItem cartItem) {
        this(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                ProductDTO.fromProduct(cartItem.getProduct()),
                cartItem.getQuantity()
        );
    }
}