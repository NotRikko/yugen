package rikko.yugen.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.product.ProductUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.*;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CollectionRepository;
import rikko.yugen.repository.SeriesRepository;
import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;

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


    // Read

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductDTO::new);
    }

    public Page<ProductDTO> getProductsOfCurrentArtist(Pageable pageable) {
        User currentUser = currentUserHelper.getCurrentUser();
        Artist artist = currentUser.getArtist();

        if (artist == null) {
            throw new RuntimeException("Artist not found for current user");
        }

        return productRepository.findByArtistId(artist.getId(), pageable)
                .map(ProductDTO::new);

    }

    public Page<ProductDTO> getProductsByArtistName(String artistName, Pageable pageable) {
        return productRepository.findByArtist_ArtistName(artistName, pageable)
                .map(ProductDTO::new);
    }

    public Page<ProductDTO> getProductsByArtistId(Long artistId, Pageable pageable) {
        return productRepository.findByArtistId(artistId, pageable)
                .map(ProductDTO::new);
    }


    public Page<ProductDTO> getProductsByCollectionName(String collectionName, Pageable pageable) {
        return productRepository.findByCollections_Name(collectionName, pageable)
                .map(ProductDTO::new);
    }


    //Create

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

        uploadAndSaveFilesForProduct(files, savedProduct.getId());


        return new ProductDTO(savedProduct);

    }

    // Update

    @Transactional
    public ProductDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO, List<MultipartFile> newFiles) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (productUpdateDTO.getName() != null) product.setName(productUpdateDTO.getName());
        if (productUpdateDTO.getDescription() != null) product.setDescription(productUpdateDTO.getDescription());
        if (productUpdateDTO.getPrice() != null) product.setPrice(productUpdateDTO.getPrice());
        if (productUpdateDTO.getQuantityInStock() != null) product.setQuantityInStock(productUpdateDTO.getQuantityInStock());

        if (productUpdateDTO.getSeries() != null) {
            Set<Series> seriesSet = productUpdateDTO.getSeries().stream()
                    .map(dto -> seriesRepository.findById(dto.id())
                            .orElseThrow(() -> new RuntimeException("Series not found")))
                    .collect(Collectors.toSet());
            product.setSeries(seriesSet);
        }

        if (productUpdateDTO.getCollections() != null) {
            Set<Collection> collectionsSet = productUpdateDTO.getCollections().stream()
                    .map(dto -> collectionRepository.findById(dto.id())
                            .orElseThrow(() -> new RuntimeException("Collection not found")))
                    .collect(Collectors.toSet());
            product.setCollections(collectionsSet);
        }

        Set<Long> imagesToKeep = productUpdateDTO.getExistingImageIds() != null
                ? new HashSet<>(productUpdateDTO.getExistingImageIds())
                : Collections.emptySet();

        List<Image> imagesToDelete = product.getImages().stream()
                .filter(image -> !imagesToKeep.contains(image.getId()))
                .toList();

        for (Image image : imagesToDelete) {
            imageService.deleteImage(image.getId());
        }

        uploadAndSaveFilesForProduct(newFiles, product.getId());

        product = productRepository.save(product);

        return new ProductDTO(product);
    }

    // Purchase test function

    @Transactional
    public String purchaseProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",  "id", productId));

        return "Product '" + product.getName() + "' bought!";
    }

    // Delete

    @Transactional
    public void deleteProduct(Long productId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",  "id", productId));

        if (!product.getArtist().getUser().equals(currentUser)) {
            throw new AccessDeniedException("You are not allowed to delete this product.");
        }

        for (Image image : product.getImages()) {
            imageService.deleteImage(image.getId());
        }

        productRepository.delete(product);
    }

    // Helpers

    private void uploadAndSaveFilesForProduct(List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    imageService.createImageForProduct(uploadedUrl, product);
                } catch (Exception e) {
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }
    }
}
