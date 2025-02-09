
package rikko.yugen.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CollectionRepository;
import rikko.yugen.repository.SeriesRepository;

import rikko.yugen.dto.ProductCreateDTO;

import rikko.yugen.model.Product;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Collection;
import rikko.yugen.model.Series;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByArtistName(String artistName) {
        return productRepository.findByArtist_ArtistName(artistName);
    }

    public List<Product> getProductsByCollectionName(String collectionName) {
        return productRepository.findByCollections_Name(collectionName);
    }

    @Transactional
    public Product createProduct(ProductCreateDTO productCreateDTO) {

        Artist artist = artistRepository.findById(productCreateDTO.getArtistId())
                        .orElseThrow(() -> new RuntimeException("Artist not found with"));

        Product product = new Product();
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());
        product.setPrice(productCreateDTO.getPrice());
        product.setQuantity_in_stock(productCreateDTO.getQuantityInStock());
        product.setImage(productCreateDTO.getImage());
        product.setArtist(artist);

        if (productCreateDTO.getSeriesIds() != null) {
            List<Series> seriesList = seriesRepository.findAllById(productCreateDTO.getSeriesIds());
            Set<Series> seriesSet = new HashSet<>(seriesList);
            product.setSeries(seriesSet);
        }

        if (productCreateDTO.getCollectionIds() != null) {
            List<Collection> collectionList = collectionRepository.findAllById(productCreateDTO.getCollectionIds());
            Set<Collection> collectionSet = new HashSet<>(collectionList);
            product.setCollections(collectionSet);
        }

        return productRepository.save(product);

    }
}
