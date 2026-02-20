package rikko.yugen.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.exception.ImageDeletionException;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {
    
    private final  ImageRepository imageRepository;
    private final  CloudinaryService cloudinaryService;

    public Set<ImageDTO> getImagesForPost(Post post) {
        List<Image> images = imageRepository.findByPost(post);

        return images.stream()
                     .map(ImageDTO::new) 
                     .collect(Collectors.toSet());
    }

    public Set<ImageDTO> getImagesForProduct(Product product) {
        List<Image> images = imageRepository.findByProduct(product);

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
    public Image createImageForProduct(String imageUrl, Product product) {
        Image image = new Image();
        image.setUrl(imageUrl);
        image.setProduct(product);

        product.getImages().add(image);

        return imageRepository.save(image);
    }


    @Transactional
    public Image createImageForUser(String imageUrl, User user) {
        Image image = new Image();
        image.setUrl(imageUrl);
        image.setUser(user);

        if (user.getProfileImage() != null) {
            imageRepository.delete(user.getProfileImage());
        }

        user.setProfileImage(image);

        return imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageDeletionException("Image not found"));

        boolean deletedFromCloud = cloudinaryService.deleteImage(image.getUrl());
        if (deletedFromCloud) {
            imageRepository.deleteById(imageId);
        } else {
            throw new ImageDeletionException("Failed to delete image from Cloudinary");
        }
    }

}
