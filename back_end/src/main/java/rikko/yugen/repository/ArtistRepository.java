package rikko.yugen.repository;
import rikko.yugen.model.Artist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByArtistName(String artistName);

}
