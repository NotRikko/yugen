package rikko.yugen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import rikko.yugen.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}