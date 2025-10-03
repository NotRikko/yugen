package rikko.yugen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.Product;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByArtist_Id(Long artistId);
    List<Post> findByProduct(Product product);
    List<Post> findByProductId(Long productId);

}
