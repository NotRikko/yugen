package rikko.yugen.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByName(String name);
    Optional<User> findByUsername(String username);
}
