package rikko.yugen.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    private final Cloudinary cloudinary;

    public CloudinaryService(
        @Value("${cloudinary.cloud_name}") String cloudinaryName,
        @Value("${cloudinary.api_key}") String apiKey,
        @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudinaryName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object>uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("secure_url");
    
        System.out.println("Uploaded image URL: " + imageUrl);
        
        return imageUrl;
    }

    @SuppressWarnings("unchecked")
    public String deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        Map<String, String> response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        if ("ok".equals(response.get("result"))) {
            return "Image deleted successfully";
        } else {
            return "Error deleting image";
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] urlParts = imageUrl.split("/");
        String publicIdWithExtension = urlParts[urlParts.length - 1];  
        String publicId = publicIdWithExtension.split("\\.")[0]; 
        return publicId;
    }
   
}