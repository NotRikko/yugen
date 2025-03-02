package rikko.yugen.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.model.Post;

import rikko.yugen.service.PostService;
import rikko.yugen.service.LikeService;
import rikko.yugen.service.ImageService;

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
            PostDTO postDTO = postService.createPost(postCreateDTO, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create post: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete post: " + e.getMessage()));
        }
    }
}
