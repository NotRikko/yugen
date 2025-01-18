package rikko.yugen.repository;
import rikko.yugen.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long>{
    Artist findByName(String name);

}
