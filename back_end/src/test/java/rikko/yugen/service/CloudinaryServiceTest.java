package rikko.yugen.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import rikko.yugen.exception.ExternalServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile mockFile;

    private final String fakeImageUrl = "https://res.cloudinary.com/demo/image/upload/v123456/test.jpg";

    @BeforeEach
    void setUp() {
        lenient().when(cloudinary.uploader()).thenReturn(uploader);
    }

    // Upload tests

    @Nested
    class UploadImageTests {
        @Test
        void uploadImage_shouldReturnSecureUrl() throws Exception {
            byte[] fileBytes = "fake content".getBytes();
            when(mockFile.getBytes()).thenReturn(fileBytes);

            Map<String, Object> uploadResult = new HashMap<>();
            uploadResult.put("secure_url", fakeImageUrl);
            when(uploader.upload(eq(fileBytes), anyMap())).thenReturn(uploadResult);

            String result = cloudinaryService.uploadImage(mockFile);

            assertEquals(fakeImageUrl, result);
            verify(uploader, times(1)).upload(eq(fileBytes), anyMap());
        }

        @Test
        void uploadImage_shouldThrowIOException_whenFileFails() throws Exception {
            when(mockFile.getBytes()).thenThrow(new IOException("Failed"));

            assertThrows(IOException.class, () -> cloudinaryService.uploadImage(mockFile));
        }
    }


    // Delete tests

    @Nested
    class DeleteImageTests {

        @Test
        void deleteImage_shouldCallUploader_whenResultOk() throws Exception {
            Map<String, String> response = Map.of("result", "ok");
            when(uploader.destroy(anyString(), anyMap())).thenReturn(response);

            cloudinaryService.deleteImage(fakeImageUrl);

            verify(uploader).destroy(anyString(), anyMap());
        }

        @Test
        void deleteImage_shouldCallUploader_whenResultNotFound() throws Exception {
            Map<String, String> response = Map.of("result", "not found");
            when(uploader.destroy(anyString(), anyMap())).thenReturn(response);

            cloudinaryService.deleteImage(fakeImageUrl);

            verify(uploader).destroy(anyString(), anyMap());
        }

        @Test
        void deleteImage_shouldThrowExternalServiceException_whenCloudinaryFails() throws Exception {
            when(uploader.destroy(anyString(), anyMap()))
                    .thenThrow(new RuntimeException("Cloudinary fail"));

            assertThrows(ExternalServiceException.class,
                    () -> cloudinaryService.deleteImage(fakeImageUrl));

            verify(uploader).destroy(anyString(), anyMap());
        }

        @Test
        void deleteImage_shouldThrowIllegalArgumentException_forInvalidUrl() {
            String invalidUrl = "https://example.com/no-upload-path.jpg";

            assertThrows(IllegalArgumentException.class,
                    () -> cloudinaryService.deleteImage(invalidUrl));
        }
    }
}