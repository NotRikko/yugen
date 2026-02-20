package rikko.yugen.repository;

import rikko.yugen.model.Image;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);

    Optional<Image> findByUser(User user);

    Optional<Image> findByProfileForArtist(Artist artist);
    Optional<Image> findByBannerForArtist(Artist artist);

    List<Image> findByProduct(Product product);
}
