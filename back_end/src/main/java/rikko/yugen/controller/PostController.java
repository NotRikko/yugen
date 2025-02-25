package rikko.yugen.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import rikko.yugen.dto.LikeDTO;
import rikko.yugen.dto.PostDTO;
import rikko.yugen.dto.PostCreateDTO;
import rikko.yugen.dto.ImageDTO;
import rikko.yugen.model.Image;
import rikko.yugen.model.Post;

import rikko.yugen.service.PostService;
import rikko.yugen.service.LikeService;
import rikko.yugen.service.ImageService;
import rikko.yugen.service.CloudinaryService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CloudinaryService cloudinaryService;

   @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        
        List<PostDTO> postDTOs = posts.stream()
                                      .map(post -> {
                                          Set<ImageDTO> imageDTOs = imageService.getImagesForPost(post.getId());
                                          Set<LikeDTO> likeDTOs = likeService.getLikesForPost(post.getId());
                                          return new PostDTO(post, likeDTOs, imageDTOs);
                                      })
                                      .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
    @RequestPart("post") PostCreateDTO postCreateDTO, 
    @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            Post createdPost = postService.createPost(postCreateDTO);
            Set<ImageDTO> imageDTOs = new HashSet<>();

            // Process image URLs
            if (postCreateDTO.getImages() != null) {
                for (String imageUrl : postCreateDTO.getImages()) {
                    Long contentId = createdPost.getId();
                    String contentType = "post";
                    Image image = imageService.createImage(imageUrl, contentType, contentId);
                    imageDTOs.add(new ImageDTO(image));
                }
            }

            // Process uploaded files
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    String uploadedUrl = cloudinaryService.uploadImage(file);
                    Long contentId = createdPost.getId();
                    String contentType = "post";
                    Image image = imageService.createImage(uploadedUrl, contentType, contentId);
                    imageDTOs.add(new ImageDTO(image));
                }
            }

            PostDTO postDTO = new PostDTO(createdPost, new HashSet<>(), imageDTOs);
            return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create post: " + e.getMessage()));
        }
    }
}
