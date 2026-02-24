package rikko.yugen.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"artist", "profileImage"})
    Optional<User> findByDisplayName(String displayName);

    @EntityGraph(attributePaths = {"artist", "profileImage"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"artist", "profileImage"})
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"artist", "profileImage"})
    Page<User> findAll(Pageable pageable);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}