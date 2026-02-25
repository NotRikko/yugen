package rikko.yugen.service;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;

import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;

import rikko.yugen.model.Artist;
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

    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    private final CurrentUserHelper currentUserHelper;

    // Read

    @Transactional(readOnly = true)
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostDTO::new);
    }


    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByArtistName(String artistName, Pageable pageable) {
        return postRepository.findByArtist_ArtistName(artistName, pageable)
                .map(PostDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByArtistId(Long artistId, Pageable pageable) {
        return postRepository.findByArtist_Id(artistId, pageable)
                .map(PostDTO::new);
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return new PostDTO(post);
    }

    // Create
    @Transactional
    public PostDTO createPost(PostCreateDTO dto, List<MultipartFile> files) {
        User currentUser = currentUserHelper.getCurrentUser();

        if (!"ARTIST".equals(currentUser.getRole().name())) {
            throw new AccessDeniedException("Only artists can create posts");
        }

        Artist artist = artistRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "userId", currentUser.getId()));

        Product product = null;
        if (dto.getProductId() != null) {
            product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", dto.getProductId()));
        }

        Post post = new Post();
        post.setContent(dto.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setArtist(artist);
        post.setProduct(product);

        Post savedPost = postRepository.save(post);

        uploadAndSaveFiles(files, savedPost);

        return new PostDTO(savedPost);
    }

    // Update

    @Transactional
    public PostDTO updatePost(Long id, PostUpdateDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
            postRepository.save(post);
        }

        return new PostDTO(post);
    }

    // Delete

    @Transactional
    public void deletePost(Long postId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (!post.getArtist().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    // Helper

    private void uploadAndSaveFiles(List<MultipartFile> files, Post post) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            try {
                String uploadedUrl = cloudinaryService.uploadImage(file);
                imageService.createImageForPost(uploadedUrl, post);
            } catch (Exception e) {
                System.err.println("Failed to upload image: " + e.getMessage());
            }
        }
    }

}