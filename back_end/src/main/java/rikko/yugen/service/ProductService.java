
package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.repository.ProductRepository;

import rikko.yugen.model.Product;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByArtistName(String artistName) {
        return productRepository.findByArtist_ArtistName(artistName);
    }

    public List<Product> getProductsByCollectionName(String collectionName) {
        return productRepository.findByCollectionName(collectionName);
    }
}
