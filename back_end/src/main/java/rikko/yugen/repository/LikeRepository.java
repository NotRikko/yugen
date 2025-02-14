package rikko.yugen.repository;

import java.util.Optional;
import java.util.Set;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Set<Like> findByContentIdAndContentType(Long contentId, String contentType);
    Optional<Like> findByUserAndContentIdAndContentType(User user, Long contentId, String contentType);
}
