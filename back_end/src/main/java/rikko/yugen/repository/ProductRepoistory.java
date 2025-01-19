package rikko.yugen.repository;
import rikko.yugen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepoistory extends JpaRepository<Product, Long>{
    Product findByName(String name);
    List<Product> findByArtistName(String artistName);
    List<Product> findByCollection(String collection);
}