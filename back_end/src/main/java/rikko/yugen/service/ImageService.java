package rikko.yugen.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.repository.ImageRepository;

@Service
public class ImageService {
    
   @Autowired
    private ImageRepository imageRepository;

    public Set<ImageDTO> getImagesForPost(Post post) {
        List<Image> images = imageRepository.findByPost(post);

        // Convert to DTOs for API response
        return images.stream()
                     .map(ImageDTO::new) 
                     .collect(Collectors.toSet());
    }

    @Transactional
    public Image createImageForPost(String imageUrl, Post post) {
        Image image = new Image();
        image.setUrl(imageUrl);
        image.setPost(post);

        post.getImages().add(image);

        return imageRepository.save(image);
    }


    @Transactional
    public Image createImageForUser(String imageUrl, User user) {
        Image image = new Image();
        image.setUrl(imageUrl);
        image.setUser(user);

        if (user.getImage() != null) {
            imageRepository.delete(user.getImage());
        }

        user.setImage(image);

        return imageRepository.save(image);
    }

}
