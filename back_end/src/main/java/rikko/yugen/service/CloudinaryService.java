package rikko.yugen.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    private final Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);


    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object>uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("secure_url");
        logger.info("Uploaded image URL: {}", imageUrl);
        return imageUrl;
    }

    @SuppressWarnings("unchecked")
    public boolean deleteImage(String imageUrl) {
        String publicId = extractPublicIdFromUrl(imageUrl);

        try {
            Map<String, String> response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String result = response.get("result");
            return "ok".equals(result) || "not found".equals(result);
        } catch (Exception e) {
            logger.error("Failed to delete image: {}", imageUrl, e);
            return false;
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/upload/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid Cloudinary URL: " + imageUrl);
        }

        String pathAndFile = parts[1];
        if (pathAndFile.startsWith("v")) {
            int firstSlash = pathAndFile.indexOf("/");
            if (firstSlash != -1) {
                pathAndFile = pathAndFile.substring(firstSlash + 1);
            }
        }
        return pathAndFile.replaceAll("\\.[^.]+$", "");
    }
   
}