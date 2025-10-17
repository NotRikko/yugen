package rikko.yugen.service;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import rikko.yugen.dto.comment.CommentDTO;

import rikko.yugen.model.Artist;
import rikko.yugen.model.Image;
import rikko.yugen.model.Like;
import rikko.yugen.model.Post;
import rikko.yugen.model.Product;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired 
    private ImageService imageService;

    public List<Post> getPostsByArtistId(Long artistId) {
        return postRepository.findByArtist_Id(artistId);
    }

    public List<PostDTO> getPostsByArtistName(String artistName) {
        List<Post> posts = postRepository.findByArtist_ArtistName(artistName);

        List<Long> postIds = posts.stream().map(Post::getId).toList();

        List<rikko.yugen.model.Like> likes = likeRepository.findLikesForPosts(postIds);

        Map<Long, Set<LikeDTO>> likesByPost = likes.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getContentId(),
                        Collectors.mapping(LikeDTO::new, Collectors.toSet())
                ));
        return posts.stream()
                .map(post -> PostDTO.fromPost(post, likesByPost.getOrDefault(post.getId(), new HashSet<>())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        List<Long> postIds = posts.stream().map(Post::getId).toList();

        List<rikko.yugen.model.Like> likes = likeRepository.findLikesForPosts(postIds);

        Map<Long, Set<LikeDTO>> likesByPost = likes.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getContentId(),
                        Collectors.mapping(LikeDTO::new, Collectors.toSet())
                ));

        return posts.stream()
                .map(post -> PostDTO.fromPost(post, likesByPost.getOrDefault(post.getId(), new HashSet<>())))
                .collect(Collectors.toList());
    }

    public PostDTO createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files) {
        Post createdPost = savePost(postCreateDTO);

        Set<ImageDTO> imageDTOs = uploadAndSaveFiles(files, createdPost.getId());

        return new PostDTO(createdPost, new HashSet<LikeDTO>(), imageDTOs, new ArrayList<CommentDTO>());
    }

    @Transactional
    private Post savePost(PostCreateDTO postCreateDTO) {
        Artist artist = artistRepository.findById(postCreateDTO.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        Product product = (postCreateDTO.getProductId() != null)
                ? productRepository.findById(postCreateDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"))
                : null;        

        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setArtist(artist);
        post.setProduct(product);    

        return postRepository.save(post);
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
                    // Log and continue, ensuring a failed upload doesn’t affect DB transactions
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }
        return imageDTOs;
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
