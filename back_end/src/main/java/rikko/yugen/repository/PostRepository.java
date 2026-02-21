package rikko.yugen.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;

import rikko.yugen.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"images", "artist", "product"})
    Page<Post> findByArtist_Id(Long artistId, Pageable pageable);
    Page<Post> findByArtist_ArtistName(String artistName, Pageable pageable);
    Page<Post> findByArtist_IdIn(List<Long> artistIds, Pageable pageable);
}
