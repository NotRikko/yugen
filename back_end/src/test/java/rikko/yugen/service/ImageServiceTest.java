package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.exception.ExternalServiceException;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CloudinaryService cloudinaryService;


    @InjectMocks
    private ImageService imageService;

    // Mocks

    private Post post;
    private Product product;
    private User user;
    private Image image;

    @BeforeEach
    void setUp() {

        post = new Post();
        post.setId(1L);
        post.setImages(new ArrayList<>());

        product = new Product();
        product.setId(1L);
        product.setImages(new ArrayList<>());

        user = new User();
        user.setId(1L);

        image = new Image();
        image.setId(1L);
        image.setUrl("http://example.com/image.jpg");
    }

    // Get tests

    @Nested
    class GetImagesTests {

        @Test
        void getImagesForPost_shouldReturnImageDTOs() {
            post.getImages().add(image);
            List<ImageDTO> dtos = imageService.getImagesForPost(post);
            assertEquals(1, dtos.size());
            assertEquals(image.getUrl(), dtos.get(0).url());
        }
    }

    // Create tests

    @Nested
    class CreateImagesTests {

        @Test
        void createImageForPost_shouldSaveImage() {
            when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

            imageService.createImageForPost("http://example.com/image.jpg", post);

            assertEquals(1, post.getImages().size());
            assertEquals("http://example.com/image.jpg", post.getImages().get(0).getUrl());
            verify(imageRepository, times(1)).save(any(Image.class));
        }

        @Test
        void createImageForPost_shouldThrowIfDuplicate() {
            post.getImages().add(image);
            assertThrows(IllegalArgumentException.class, () ->
                    imageService.createImageForPost("http://example.com/image.jpg", post)
            );
        }

        @Test
        void createImageForUser_shouldReplaceOldImage() {
            Image oldImage = new Image();
            oldImage.setUrl("http://example.com/old.jpg");
            user.setProfileImage(oldImage);

            when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ImageDTO dto = imageService.createImageForUser("http://example.com/new.jpg", user);

            assertEquals("http://example.com/new.jpg", dto.url());
            assertEquals(user.getProfileImage().getUrl(), dto.url());
            verify(cloudinaryService, times(1)).deleteImage("http://example.com/old.jpg");
            verify(imageRepository, times(1)).delete(oldImage);
        }
    }

    // Delete tests

    @Nested
    class DeleteImagesTests {
        @Test
        void deleteImage_shouldDeleteSuccessfully() {
            image.setPost(post);
            post.getImages().add(image);

            when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

            imageService.deleteImage(1L);

            verify(cloudinaryService, times(1)).deleteImage(image.getUrl());
            verify(imageRepository, times(1)).delete(image);
            assertTrue(post.getImages().isEmpty());
        }

        @Test
        void deleteImage_shouldThrowIfNotFound() {
            when(imageRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> imageService.deleteImage(1L));
        }

        @Test
        void deleteImage_shouldThrowIfCloudDeletionFails() {
            when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

            doThrow(new ExternalServiceException("Cloudinary deletion failed"))
                    .when(cloudinaryService)
                    .deleteImage(anyString());

            assertThrows(ExternalServiceException.class,
                    () -> imageService.deleteImage(1L));

            verify(imageRepository, never()).delete(any());
        }
    }
}
