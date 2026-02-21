package rikko.yugen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikko.yugen.model.Comment;
import rikko.yugen.model.CommentLike;
import rikko.yugen.model.User;

import java.util.Optional;
import java.util.Set;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Set<CommentLike> findByComment(Comment comment);
    int countByComment(Comment comment);
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
