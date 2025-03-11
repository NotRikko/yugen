package rikko.yugen.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByDisplayName(String displayName);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
