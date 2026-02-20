package rikko.yugen.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.product.ProductUpdateDTO;
import rikko.yugen.model.*;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CollectionRepository;
import rikko.yugen.repository.SeriesRepository;
import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.image.ImageDTO;

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

    public List<ProductDTO> getProductsOfCurrentArtist() {
        User currentUser = currentUserHelper.getCurrentUser();
        Artist artist = currentUser.getArtist();

        if (artist == null) {
            throw new RuntimeException("Artist not found for current user");
        }

        List<Product> products = productRepository.findByArtistId(artist.getId());

        return products.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
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

        Set<ImageDTO> imageDTOs = uploadAndSaveFilesForProduct(files, savedProduct.getId());

        return new ProductDTO(savedProduct, imageDTOs);

    }
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

        Set<ImageDTO> newImageDTOs = uploadAndSaveFilesForProduct(newFiles, product.getId());

        product = productRepository.save(product);

        Set<ImageDTO> allImages = new HashSet<>();
        allImages.addAll(product.getImages().stream().map(ImageDTO::new).collect(Collectors.toSet()));
        allImages.addAll(newImageDTOs);

        return ProductDTO.fromProduct(product, allImages);
    }

    @Transactional
    public String purchaseProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return "Product '" + product.getName() + "' bought!";
    }

    @Transactional
    public void deleteProduct(Long productId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getArtist().getUser().equals(currentUser)) {
            throw new RuntimeException("You are not allowed to delete this product.");
        }

        for (Image image : product.getImages()) {
            imageService.deleteImage(image.getId());
        }

        productRepository.delete(product);
    }

    private Set<ImageDTO> uploadAndSaveFilesForProduct(List<MultipartFile> files, Long productId) {
        Set<ImageDTO> imageDTOs = new HashSet<>();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    ImageDTO imageDTO = imageService.createImageForProduct(uploadedUrl, product);
                    imageDTOs.add(imageDTO);
                } catch (Exception e) {
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }

        return imageDTOs;
    }
}
