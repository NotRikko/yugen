package rikko.yugen.repository;
import rikko.yugen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Product findByName(String name);
    List<Product> findByArtist_ArtistName(String artistName);
    List<Product> findByCollectionName(String collectionName);
}