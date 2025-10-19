package rikko.yugen.bootstrap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Product;
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
            p1.setImage("https://pbs.twimg.com/media/GzhOWSpWcAAxTrI.png");
            p1.setArtist(artists.get(0));

            Product p2 = new Product();
            p2.setName("Art Print");
            p2.setPrice(29.99f);
            p2.setImage("https://pbs.twimg.com/media/GzhOWSpWcAAxTrI.png");
            p2.setArtist(artists.get(1));

            productRepository.saveAll(List.of(p1, p2));
        }
    }
}