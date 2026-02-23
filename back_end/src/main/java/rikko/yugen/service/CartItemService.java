package rikko.yugen.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.CartItem;

import rikko.yugen.repository.CartItemRepository;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    // Get

    @Transactional(readOnly = true)
    public List<CartItem> getItemsByCartId(Long cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }

    @Transactional(readOnly = true)
    public CartItem getItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", id));
    }

    // Write

    @Transactional
    public CartItem save(CartItem item) {
        if (item == null) throw new IllegalArgumentException("CartItem cannot be null");
        return cartItemRepository.save(item);
    }

    // Delete

    @Transactional
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("CartItem id cannot be null");
        if (!cartItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("CartItem", "id", id);
        }
        cartItemRepository.deleteById(id);
    }
}