package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
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


    // Mock Artist and user

    private User mockUser;
    private Artist mockArtist;


    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");
        mockUser.setDisplayName("Rikko");
        mockUser.setEmail("rikko@test.com");

        mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setArtistName("Rikko");
        mockArtist.setBio("I am a test");

        mockArtist.setUser(mockUser);
        mockUser.setIsArtist(true);
        mockUser.setArtist(mockArtist);
    }

    // Get by id tests

    @Test
    void getProductById_shouldReturnProductDTO_whenProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setDescription("Description");
        product.setPrice(10.99F);
        product.setQuantityInStock(10);
        product.setArtist(mockArtist);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test", result.name());
        assertEquals("Description", result.description());
        assertEquals(10.99F, result.price());
        assertEquals(10, result.quantityInStock());
        assertNotNull(result.artist());
        assertEquals(mockArtist.getId(), result.artist().id());
        assertEquals(mockArtist.getArtistName(), result.artist().artistName());

        verify(productRepository).findById(1L);
    }

    @Test
    void getProductId_shouldThrowException_whenPostDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    // Get products of current artist tests

    @Test
    void getProductsOfCurrentArtist_shouldReturnPageOfProductDTO_ifCurrentArtistExists() {
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setDescription("Description");
        product.setPrice(10.99F);
        product.setQuantityInStock(10);
        product.setArtist(mockArtist);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test2");
        product2.setDescription("Description2");
        product2.setPrice(12.99F);
        product2.setQuantityInStock(12);
        product2.setArtist(mockArtist);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product, product2));
        when(productRepository.findByArtistId(mockArtist.getId(), pageable))
                .thenReturn(productPage);

        Page<ProductDTO> result = productService.getProductsOfCurrentArtist(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        ProductDTO first = result.getContent().get(0);
        assertEquals(1L, first.id());
        assertEquals("Test", first.name());
        assertEquals("Description", first.description());
        assertEquals(10.99F, first.price());
        assertEquals(10, first.quantityInStock());
        assertEquals(mockArtist.getId(), first.artist().id());

        ProductDTO second = result.getContent().get(1);
        assertEquals(2L, second.id());
        assertEquals("Test2", second.name());
        assertEquals("Description2", second.description());
        assertEquals(12.99F, second.price());
        assertEquals(12, second.quantityInStock());
        assertEquals(mockArtist.getId(), second.artist().id());

        verify(productRepository).findByArtistId(mockArtist.getId(), pageable);
    }

    @Test
    void getProductsOfCurrentArtist_shouldThrowResourceNotFoundException_whenUserHasNoArtist() {
        User userWithoutArtist = new User();
        userWithoutArtist.setId(2L);
        userWithoutArtist.setIsArtist(false);
        userWithoutArtist.setArtist(null);

        when(currentUserHelper.getCurrentUser()).thenReturn(userWithoutArtist);

        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductsOfCurrentArtist(pageable));

        verify(productRepository, never()).findByArtistId(anyLong(), any());
    }

    @Test
    void getProductsOfCurrentArtist_shouldReturnEmptyPage_whenNoProductsExist() {
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(productRepository.findByArtistId(mockArtist.getId(), pageable))
                .thenReturn(emptyPage);

        Page<ProductDTO> result = productService.getProductsOfCurrentArtist(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findByArtistId(mockArtist.getId(), pageable);
    }

    // Get by artistId tests

    @Test
    void getProductsByArtistId_shouldReturnPaginatedProductDTOs() {
        Long artistId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Desc 1");
        product1.setPrice(9.99F);
        product1.setQuantityInStock(5);
        product1.setArtist(mockArtist);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Desc 2");
        product2.setPrice(19.99F);
        product2.setQuantityInStock(10);
        product2.setArtist(mockArtist);

        Page<Product> productPage = new PageImpl<>(
                List.of(product1, product2),
                pageable,
                2
        );

        when(productRepository.findByArtistId(artistId, pageable))
                .thenReturn(productPage);

        Page<ProductDTO> result =
                productService.getProductsByArtistId(artistId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        ProductDTO first = result.getContent().get(0);
        assertEquals(1L, first.id());
        assertEquals("Product 1", first.name());

        ProductDTO second = result.getContent().get(1);
        assertEquals(2L, second.id());
        assertEquals("Product 2", second.name());

        verify(productRepository).findByArtistId(artistId, pageable);
    }

    @Test
    void getProductsByArtistId_shouldReturnEmptyPage_whenNoProductsExist() {
        Long artistId = 99L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(productRepository.findByArtistId(artistId, pageable))
                .thenReturn(emptyPage);

        Page<ProductDTO> result =
                productService.getProductsByArtistId(artistId, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(productRepository).findByArtistId(artistId, pageable);
    }

    // Create product test

    @Test
    void createProduct_shouldCreateAndReturnProductDTO_whenValid() {
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        when(artistRepository.findByUserId(mockUser.getId()))
                .thenReturn(Optional.of(mockArtist));

        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPrice(19.99F);
        dto.setQuantityInStock(5);

        Product savedProduct = new Product();
        savedProduct.setId(100L);
        savedProduct.setName("Test Product");
        savedProduct.setDescription("Test Description");
        savedProduct.setPrice(19.99F);
        savedProduct.setQuantityInStock(5);
        savedProduct.setArtist(mockArtist);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        ProductDTO result =
                productService.createProduct(dto, null);

        assertNotNull(result);
        assertEquals("Test Product", result.name());
        assertEquals("Test Description", result.description());
        assertEquals(19.99F, result.price());
        assertEquals(5, result.quantityInStock());

        verify(currentUserHelper).getCurrentUser();
        verify(artistRepository).findByUserId(mockUser.getId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_shouldAttachSeriesAndCollections_whenIdsProvided() {
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
        when(artistRepository.findByUserId(mockUser.getId()))
                .thenReturn(Optional.of(mockArtist));

        Series series1 = new Series();
        series1.setId(1L);

        Collection collection1 = new Collection();
        collection1.setId(10L);

        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("Product");
        dto.setPrice(10F);
        dto.setQuantityInStock(2);
        dto.setSeriesIds(Set.of(1L));
        dto.setCollectionIds(Set.of(10L));

        when(seriesRepository.findAllById(anyCollection()))
                .thenReturn(List.of(series1));

        when(collectionRepository.findAllById(anyCollection()))
                .thenReturn(List.of(collection1));

        Product savedProduct = new Product();
        savedProduct.setId(200L);
        savedProduct.setArtist(mockArtist);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);;

        ProductDTO result = productService.createProduct(dto, null);

        assertNotNull(result);

        verify(seriesRepository).findAllById(anyCollection());
        verify(collectionRepository).findAllById(anyCollection());
        verify(productRepository).save(any(Product.class));
    }

    // Update product tests
    @Test
    void updateProduct_shouldUpdateFieldsSeriesCollectionsAndImages() throws Exception {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Desc");
        existingProduct.setPrice(10F);
        existingProduct.setQuantityInStock(5);

        Series oldSeries = new Series();
        oldSeries.setId(100L);
        existingProduct.setSeries(Set.of(oldSeries));

        Collection oldCollection = new Collection();
        oldCollection.setId(200L);
        existingProduct.setCollections(Set.of(oldCollection));

        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(2L);
        existingProduct.setImages(Set.of(image1, image2)); // must be List

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        Series newSeriesEntity = new Series();
        newSeriesEntity.setId(101L);

        Collection newCollectionEntity = new Collection();
        newCollectionEntity.setId(201L);

        SeriesDTO newSeriesDTO = new SeriesDTO(newSeriesEntity);
        CollectionDTO newCollectionDTO = new CollectionDTO(newCollectionEntity);

        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Desc");
        updateDTO.setPrice(20F);
        updateDTO.setQuantityInStock(10);
        updateDTO.setSeries(Set.of(newSeriesDTO));
        updateDTO.setCollections(Set.of(newCollectionDTO));
        updateDTO.setExistingImageIds(Set.of(1L));

        when(seriesRepository.findById(101L)).thenReturn(Optional.of(newSeriesEntity));
        when(collectionRepository.findById(201L)).thenReturn(Optional.of(newCollectionEntity));

        doNothing().when(imageService).deleteImage(2L);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<MultipartFile> newFiles = List.of();

        ProductDTO result = productService.updateProduct(1L, updateDTO, newFiles);

        assertEquals("New Name", result.name());
        assertEquals("New Desc", result.description());
        assertEquals(20F, result.price());
        assertEquals(10, result.quantityInStock());

        assertEquals(1, result.series().size());
        assertTrue(result.series().stream().anyMatch(s -> s.id() == 101L));

        assertEquals(1, result.collections().size());
        assertTrue(result.collections().stream().anyMatch(c -> c.id() == 201L));

        verify(imageService).deleteImage(2L);

        verify(productRepository).save(existingProduct);
    }

    // Delete product tests

    @Test
    void deleteProduct_shouldCallRepositoryDeleteAndImages_whenUserOwnsProduct() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        Artist mockArtist = new Artist();
        mockArtist.setUser(mockUser);

        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(2L);

        Product mockProduct = new Product();
        mockProduct.setArtist(mockArtist);
        mockProduct.setImages(Set.of(image1, image2));

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        doNothing().when(imageService).deleteImage(anyLong());

        productService.deleteProduct(1L);

        verify(imageService).deleteImage(1L);
        verify(imageService).deleteImage(2L);
        verify(productRepository).delete(mockProduct);
    }

    @Test
    void deleteProduct_shouldThrowAccessDeniedException_whenUserOwnsProduct() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        User otherUser = new User();
        otherUser.setId(2L);

        Artist mockArtist = new Artist();
        mockArtist.setUser(otherUser);

        Product mockProduct = new Product();
        mockProduct.setArtist(mockArtist);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        assertThrows(AccessDeniedException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any());
    }

    // Upload and save files tests

    @Test
    void uploadAndSaveFilesForProduct_shouldUploadAndCreateImages() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

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
        verify(imageService).createImageForProduct("url1", mockProduct);
        verify(imageService).createImageForProduct("url2", mockProduct);
    }
}
