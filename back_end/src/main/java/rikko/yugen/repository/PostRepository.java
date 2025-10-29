package rikko.yugen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;

import rikko.yugen.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"images", "artist", "product"})
    List<Post> findByArtist_Id(Long artistId);
    List<Post> findByArtist_ArtistName(String artistName);
    List<Post> findByArtist_IdIn(List<Long> artistIds, Pageable pageable);
}
