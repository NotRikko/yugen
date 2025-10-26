package rikko.yugen.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CollectionRepository;
import rikko.yugen.repository.SeriesRepository;
import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.image.ImageDTO;

import rikko.yugen.model.Image;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Collection;
import rikko.yugen.model.Series;

import rikko.yugen.helpers.CurrentUserHelper;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final  ProductRepository productRepository;
    private final ArtistRepository artistRepository;
    private final SeriesRepository seriesRepository;
    private final CollectionRepository collectionRepository;

    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    private final CurrentUserHelper currentUserHelper;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByArtistName(String artistName) {
        return productRepository.findByArtist_ArtistName(artistName);
    }

    public List<Product> getProductsByArtistId(Long artistId) {
        return productRepository.findByArtistId(artistId);
    }

    public List<Product> getProductsByCollectionName(String collectionName) {
        return productRepository.findByCollections_Name(collectionName);
    }

    @Transactional
    public ProductDTO createProduct(ProductCreateDTO productCreateDTO, List<MultipartFile> files) {
        User currentUser = currentUserHelper.getCurrentUser();

        Artist artist = artistRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Artist not found for current user"));

        Product product = new Product();
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());
        product.setPrice(productCreateDTO.getPrice());
        product.setQuantityInStock(productCreateDTO.getQuantityInStock());
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

        Product savedProduct = productRepository.save(product);

        Set<ImageDTO> imageDTOs = uploadAndSaveFiles(files, savedProduct.getId());

        return new ProductDTO(savedProduct, imageDTOs);

    }

    @Transactional
    public String purchaseProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return "Product '" + product.getName() + "' bought!";
    }

    private Set<ImageDTO> uploadAndSaveFiles(List<MultipartFile> files, Long productId) {
        Set<ImageDTO> imageDTOs = new HashSet<>();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    Image image = imageService.createImageForProduct(uploadedUrl, product);
                    imageDTOs.add(new ImageDTO(image));
                } catch (Exception e) {
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }

        return imageDTOs;
    }

    @Transactional
    public void deleteProduct(Long productId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!product.getArtist().getUser().equals(currentUser)) {
            throw new RuntimeException("You are not allowed to delete this product.");
        }
        productRepository.delete(product);
    }
}
