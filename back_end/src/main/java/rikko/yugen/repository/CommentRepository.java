package rikko.yugen.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import rikko.yugen.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByPostId(Long postId);

}