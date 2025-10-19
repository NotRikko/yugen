package rikko.yugen.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import rikko.yugen.model.CartItem;

import rikko.yugen.repository.CartItemRepository;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public List<CartItem> getItemsByCartId(Long cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }

    public Optional<CartItem> getItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem save(CartItem item) {
        return cartItemRepository.save(item);
    }

    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}