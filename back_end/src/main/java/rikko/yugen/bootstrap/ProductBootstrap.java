package rikko.yugen.bootstrap;

import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Product;
import rikko.yugen.model.Image;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;

import java.util.List;


@Component
public class ProductBootstrap {

    private final ProductRepository productRepository;
    private final ArtistRepository artistRepository;

    public ProductBootstrap(ProductRepository productRepository, ArtistRepository artistRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
    }

    public void load() {
        if (productRepository.count() == 0) {
            List<Artist> artists = artistRepository.findAll();
            if (artists.isEmpty()) return;

            Product p1 = new Product();
            p1.setName("Cool Poster");
            p1.setPrice(19.99f);
            p1.setArtist(artists.get(0));

            Image img1 = new Image();
            img1.setUrl("https://pbs.twimg.com/media/GzhOWSpWcAAxTrI.png");
            img1.setProduct(p1);
            p1.getImages().add(img1);

            Product p2 = new Product();
            p2.setName("Art Print");
            p2.setPrice(29.99f);
            p2.setArtist(artists.get(1));

            Image img2 = new Image();
            img2.setUrl("https://pbs.twimg.com/media/GzhOWSpWcAAxTrI.png");
            img2.setProduct(p2);
            p2.getImages().add(img2);

            productRepository.saveAll(List.of(p1, p2));
        }
    }
}