package rikko.yugen.dto.cart;

import rikko.yugen.model.Cart;
import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private final Long id;
    private final List<CartItemDTO> items;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.items = cart.getItems() != null
                ? cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList())
                : List.of();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }
}