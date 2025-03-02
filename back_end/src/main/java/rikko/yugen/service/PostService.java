package rikko.yugen.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.LikeRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Image;
import rikko.yugen.model.Like;

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

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Set<Like> getPostLikes(Long postId) {
        return likeRepository.findByContentIdAndContentType(postId, "POST");
    }

    public PostDTO createPost(PostCreateDTO postCreateDTO, List<MultipartFile> files) {
        // Save post within a transaction
        Post createdPost = savePost(postCreateDTO);

        // Process images outside the transaction
        Set<ImageDTO> imageDTOs = uploadAndSaveFiles(files, createdPost.getId());

        return new PostDTO(createdPost, new HashSet<>(), imageDTOs);
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

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    Image image = imageService.createImage(uploadedUrl, "post", postId);
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
