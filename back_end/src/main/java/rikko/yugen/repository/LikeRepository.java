package rikko.yugen.repository;

import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByContentIdAndContentType(Long contentId, String contentType);
    Optional<Like> findByUserAndContentIdAndContentType(User user, Long contentId, String contentType);
}
