package rikko.yugen.service;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.LikeRepository;

import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.like.LikeDTO;

import rikko.yugen.model.Artist;
import rikko.yugen.model.Image;
import rikko.yugen.model.Like;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.model.User;

import rikko.yugen.helpers.CurrentUserHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ArtistRepository artistRepository;
    private final ProductRepository productRepository;
    private final LikeRepository likeRepository;

    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    private final CurrentUserHelper currentUserHelper;

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        List<Like> likes = likeRepository.findLikesForPosts(postIds);

        Map<Long, Set<LikeDTO>> likesByPost = likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getContentId,
                        Collectors.mapping(LikeDTO::new, Collectors.toSet())
                ));

        return posts.stream()
                .map(post -> PostDTO.fromPost(post, likesByPost.getOrDefault(post.getId(), new HashSet<>())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByArtistName(String artistName) {
        List<Post> posts = postRepository.findByArtist_ArtistName(artistName);
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        List<Like> likes = likeRepository.findLikesForPosts(postIds);

        Map<Long, Set<LikeDTO>> likesByPost = likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getContentId,
                        Collectors.mapping(LikeDTO::new, Collectors.toSet())
                ));

        return posts.stream()
                .map(post -> PostDTO.fromPost(post, likesByPost.getOrDefault(post.getId(), new HashSet<>())))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDTO createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files) {
        User currentUser = currentUserHelper.getCurrentUser();

        Artist artist = artistRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Artist not found for current user"));

        Product product = (postCreateDTO.getProductId() != null)
                ? productRepository.findById(postCreateDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"))
                : null;

        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setArtist(artist);
        post.setProduct(product);

        Post savedPost = postRepository.save(post);

        Set<ImageDTO> imageDTOs = uploadAndSaveFiles(files, savedPost.getId());

        return new PostDTO(savedPost, new HashSet<>(), imageDTOs, new ArrayList<>());
    }

    private Set<ImageDTO> uploadAndSaveFiles(List<MultipartFile> files, Long postId) {
        Set<ImageDTO> imageDTOs = new HashSet<>();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + postId));

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    Image image = imageService.createImageForPost(uploadedUrl, post);
                    imageDTOs.add(new ImageDTO(image));
                } catch (Exception e) {
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }

        return imageDTOs;
    }

    @Transactional
    public void deletePost(Long postId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getArtist().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this post");
        }
        postRepository.delete(post);
    }
}