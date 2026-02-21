package rikko.yugen.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.PostLike;
import rikko.yugen.model.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Set<PostLike> findByPost(Post post);
    int countByPost(Post post);
    Optional<PostLike> findByUserAndPost(User user, Post post);
}