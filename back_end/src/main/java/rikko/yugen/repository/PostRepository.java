package rikko.yugen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByArtist_Id(Long artistId);
}
