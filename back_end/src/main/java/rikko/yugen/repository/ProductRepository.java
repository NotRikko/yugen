package rikko.yugen.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rikko.yugen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Page<Product> findByArtist_ArtistName(String artistName, Pageable pageable);
    Page<Product> findByArtistId(Long artistId, Pageable pageable);
    Page<Product> findByCollections_Name(String collectionName, Pageable pageable);
    Page<Product> findByName(String name, Pageable pageable);
}