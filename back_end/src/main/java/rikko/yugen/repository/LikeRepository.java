package rikko.yugen.repository;

import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rikko.yugen.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Set<Like> findByContentIdAndContentType(Long contentId, String contentType);

    Optional<Like> findByUserIdAndContentIdAndContentType(Long userId, Long contentId, String contentType);

    int countByContentIdAndContentType(Long contentId, String contentType);

    List<Like> findByContentTypeAndContentIdIn(String contentType, List<Long> postIds);

    List<Like> findByContentTypeAndContentId(String contentType, Long postId);
}