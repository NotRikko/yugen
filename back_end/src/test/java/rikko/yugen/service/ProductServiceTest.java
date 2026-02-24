package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;
import rikko.yugen.dto.collection.CollectionDTO;
import rikko.yugen.dto.product.ProductCreateDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.product.ProductUpdateDTO;
import rikko.yugen.dto.series.SeriesDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.*;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.CollectionRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.SeriesRepository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @Mock
    private ImageService imageService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductService productService;

    // Mocks

    private Artist artist;
    private User user;
    private Product product1;
    private Product product2;
    private Series series1;
    private Series series2;
    private Collection collection1;
    private Collection collection2;
    private Image image1;
    private Image image2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Rikko");
        user.setIsArtist(true);

        artist = new Artist();
        artist.setId(1L);
        artist.setUser(user);
        user.setArtist( artist);

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setArtist( artist);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setArtist(artist);

        series1 = new Series();
        series1.setId(1L);
        series2 = new Series();
        series2.setId(2L);

        collection1 = new Collection();
        collection1.setId(10L);
        collection2 = new Collection();
        collection2.setId(11L);

        image1 = new Image();
        image1.setId(1L);
        image2 = new Image();
        image2.setId(2L);
    }
    // Test helpers

    private ProductCreateDTO createProductDTO(String name, float price) {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName(name);
        dto.setPrice(price);
        dto.setQuantityInStock(5);
        return dto;
    }

    // Get product tests

    @Nested
    class GetProductsTests {

        @Test
        void getProductById_shouldReturnProductDTO() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

            ProductDTO result = productService.getProductById(1L);

            assertEquals(1L, result.id());
            assertEquals("Product 1", result.name());
            verify(productRepository).findById(1L);
        }

        @Test
        void getProductById_shouldThrow_whenNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class,
                    () -> productService.getProductById(1L));
        }

        @Test
        void getProductsByArtistId_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Product> page = new PageImpl<>(List.of(product1, product2), pageable, 2);

            when(productRepository.findByArtistId(artist.getId(), pageable)).thenReturn(page);

            Page<ProductDTO> result = productService.getProductsByArtistId(artist.getId(), pageable);

            assertEquals(2, result.getTotalElements());
            assertEquals("Product 1", result.getContent().get(0).name());
            assertEquals("Product 2", result.getContent().get(1).name());
        }

        @Test
        void getProductsByArtistId_shouldReturnEmpty_whenNoProducts() {
            Pageable pageable = PageRequest.of(0, 10);
            when(productRepository.findByArtistId(artist.getId(), pageable))
                    .thenReturn(new PageImpl<>(List.of(), pageable, 0));

            Page<ProductDTO> result = productService.getProductsByArtistId(artist.getId(), pageable);

            assertTrue(result.isEmpty());
        }
    }

    // Create product tests

    @Nested
    class CreateProductTests {

        @Test
        void createProduct_shouldReturnProductDTO() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));

            ProductCreateDTO dto = createProductDTO("New Product", 10F);

            Product savedProduct = new Product();
            savedProduct.setId(100L);
            savedProduct.setName(dto.getName());
            savedProduct.setArtist(artist);

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            ProductDTO result = productService.createProduct(dto, null);

            assertEquals("New Product", result.name());
            verify(productRepository).save(any(Product.class));
        }

        @Test
        void createProduct_shouldAttachSeriesAndCollections() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));

            ProductCreateDTO dto = createProductDTO("With Series", 15F);
            dto.setSeriesIds(Set.of(1L));
            dto.setCollectionIds(Set.of(10L));

            when(seriesRepository.findAllById(anyCollection())).thenReturn(List.of(series1));
            when(collectionRepository.findAllById(anyCollection())).thenReturn(List.of(collection1));

            Product savedProduct = new Product();
            savedProduct.setId(200L);
            savedProduct.setArtist(artist);
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            productService.createProduct(dto, null);

            verify(seriesRepository).findAllById(anyCollection());
            verify(collectionRepository).findAllById(anyCollection());
            verify(productRepository).save(any(Product.class));
        }
    }

    // Update product tests

    @Nested
    class UpdateProductTests {

        @Test
        void updateProduct_shouldUpdateFieldsAndImages() {
            product1.setDescription("Old Desc");
            product1.setPrice(10F);
            product1.setQuantityInStock(5);
            product1.setSeries(Set.of(series1));
            product1.setCollections(Set.of(collection1));
            product1.setImages(List.of(image1, image2));

            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
            when(seriesRepository.findById(2L)).thenReturn(Optional.of(series2));
            when(collectionRepository.findById(11L)).thenReturn(Optional.of(collection2));
            doNothing().when(imageService).deleteImage(anyLong());
            when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

            ProductUpdateDTO dto = new ProductUpdateDTO();
            dto.setName("Updated Name");
            dto.setPrice(20F);
            dto.setSeries(Set.of(new SeriesDTO(series2)));
            dto.setCollections(Set.of(new CollectionDTO(collection2)));
            dto.setExistingImageIds(Set.of(1L));
            List<MultipartFile> newFiles = List.of();

            ProductDTO result = productService.updateProduct(1L, dto, newFiles);

            assertEquals("Updated Name", result.name());
            assertEquals(20F, result.price());
            assertEquals(1, result.seriesIds().size());
            assertEquals(1, result.collectionIds().size());
            verify(imageService).deleteImage(2L);
        }
    }

    // Delete product tests

    @Nested
    class DeleteProductTests {

        @Test
        void deleteProduct_shouldDeleteProductAndImagesWhenUserOwnsProduct() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);

            Product product = new Product();
            product.setArtist(artist);
            product.setImages(List.of(image1, image2));

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            doNothing().when(imageService).deleteImage(anyLong());

            productService.deleteProduct(1L);

            verify(imageService).deleteImage(1L);
            verify(imageService).deleteImage(2L);
            verify(productRepository).delete(product);
        }

        @Test
        void deleteProduct_shouldThrowAccessDenied_whenUserDoesNotOwnProduct() {
            User otherUser = new User();
            otherUser.setId(2L);
            Artist otherArtist = new Artist();
            otherArtist.setUser(otherUser);
            Product product = new Product();
            product.setArtist(otherArtist);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(currentUserHelper.getCurrentUser()).thenReturn(user);

            assertThrows(AccessDeniedException.class, () -> productService.deleteProduct(1L));
            verify(productRepository, never()).delete(any());
        }
    }

    // Upload and save files tests

    @Nested
    class UploadFilesTests {

        @Test
        void uploadAndSaveFilesForProduct_shouldCallCloudinaryAndImageService() throws Exception {
            Product product = new Product();
            product.setId(1L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            MultipartFile file1 = mock(MultipartFile.class);
            MultipartFile file2 = mock(MultipartFile.class);
            List<MultipartFile> files = List.of(file1, file2);

            when(cloudinaryService.uploadImage(file1)).thenReturn("url1");
            when(cloudinaryService.uploadImage(file2)).thenReturn("url2");

            Method method = ProductService.class.getDeclaredMethod(
                    "uploadAndSaveFilesForProduct", List.class, Long.class
            );
            method.setAccessible(true);
            method.invoke(productService, files, 1L);

            verify(cloudinaryService).uploadImage(file1);
            verify(cloudinaryService).uploadImage(file2);
            verify(imageService).createImageForProduct("url1", product);
            verify(imageService).createImageForProduct("url2", product);
        }
    }
}
