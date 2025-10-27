package rikko.yugen.dto.cart;

import rikko.yugen.model.Cart;
import java.util.List;
import java.util.stream.Collectors;

public record CartDTO(Long id, List<CartItemDTO> items) {
    public CartDTO(Cart cart) {
        this(
                cart.getId(),
                cart.getItems() != null
                        ? cart.getItems().stream()
                        .map(CartItemDTO::new)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}