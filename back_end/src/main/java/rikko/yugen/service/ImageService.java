package rikko.yugen.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.exception.ImageDeletionException;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;
import rikko.yugen.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    // Get all images for a post

    @Transactional(readOnly = true)
    public List<ImageDTO> getImagesForPost(Post post) {
        return post.getImages().stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    // Get all images for a product

    @Transactional(readOnly = true)
    public List<ImageDTO> getImagesForProduct(Product product) {
        return product.getImages().stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
    }

    // Create and save an image for a post

    @Transactional
    public void createImageForPost(String imageUrl, Post post) {
        if (post.getImages().stream().anyMatch(img -> img.getUrl().equals(imageUrl))) {
            throw new IllegalArgumentException("Image already exists for this post");
        }

        Image image = new Image();
        image.setUrl(imageUrl);
        image.setPost(post);

        post.getImages().add(image);

        new ImageDTO(imageRepository.save(image));
    }

    // Create and save an image for a product

    @Transactional
    public void createImageForProduct(String imageUrl, Product product) {
        if (product.getImages().stream().anyMatch(img -> img.getUrl().equals(imageUrl))) {
            throw new IllegalArgumentException("Image already exists for this product");
        }

        Image image = new Image();
        image.setUrl(imageUrl);
        image.setProduct(product);

        product.getImages().add(image);

        new ImageDTO(imageRepository.save(image));
    }

    // Create or replace a User's profile image

    @Transactional
    public ImageDTO createImageForUser(String imageUrl, User user) {
        Optional.ofNullable(user.getProfileImage()).ifPresent(oldImage -> {
            cloudinaryService.deleteImage(oldImage.getUrl());
            user.setProfileImage(null);
            imageRepository.delete(oldImage);
        });

        Image image = new Image();
        image.setUrl(imageUrl);
        image.setUser(user);
        user.setProfileImage(image);

        return new ImageDTO(imageRepository.save(image));
    }

    // Delete an image from Cloudinary and database

    @Transactional
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "imageID", imageId));

        boolean deletedFromCloud = cloudinaryService.deleteImage(image.getUrl());
        if (!deletedFromCloud) {
            throw new ImageDeletionException("Failed to delete image from Cloudinary");
        }

        if (image.getUser() != null) image.getUser().setProfileImage(null);
        if (image.getPost() != null) image.getPost().getImages().remove(image);
        if (image.getProduct() != null) image.getProduct().getImages().remove(image);
        if (image.getProfileForArtist() != null) image.getProfileForArtist().setProfileImage(null);
        if (image.getBannerForArtist() != null) image.getBannerForArtist().setBannerImage(null);

        imageRepository.delete(image);
    }
}