package rikko.yugen.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.dto.ImageDTO;
import rikko.yugen.model.Image;
import rikko.yugen.repository.ImageRepository;

@Service
public class ImageService {
    
   @Autowired
    private ImageRepository imageRepository;

    public Set<ImageDTO> getImagesForPost(Long postId) {
        List<Image> images = imageRepository.findByContentIdAndContentType(postId, "post");

        // Convert to DTOs for API response
        return images.stream()
                     .map(ImageDTO::new) 
                     .collect(Collectors.toSet());
    }

    public Set<ImageDTO> getImagesForComment(Long commentId) {
        List<Image> images = imageRepository.findByContentIdAndContentType(commentId, "comment");

        // Convert to DTOs for API response
        return images.stream()
                     .map(ImageDTO::new) 
                     .collect(Collectors.toSet());
    }
}
