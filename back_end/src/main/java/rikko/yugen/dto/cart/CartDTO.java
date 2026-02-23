package rikko.yugen.dto.cart;

import rikko.yugen.model.Cart;
import java.util.List;

public record CartDTO(
        Long id,
        Long userId,
        List<CartItemDTO> items
) {
    public CartDTO(Cart cart) {
        this(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getId() : null,
                cart.getItems() != null
                        ? cart.getItems().stream()
                        .map(CartItemDTO::new)
                        .toList()
                        : List.of()
        );
    }
}