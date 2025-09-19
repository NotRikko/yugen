package rikko.yugen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import rikko.yugen.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

}