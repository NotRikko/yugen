package rikko.yugen.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentRequestDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.model.Post;
import rikko.yugen.model.Comment;

import rikko.yugen.service.PostService;
import rikko.yugen.service.LikeService;
import rikko.yugen.service.ImageService;
import rikko.yugen.service.CommentService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;

    private final LikeService likeService;

    private final CommentService commentService;

    private final ImageService imageService;

   @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        
        List<PostDTO> postDTOs = posts.stream()
                                      .map(post -> {
                                          Set<ImageDTO> imageDTOs = imageService.getImagesForPost(post);
                                          Set<LikeDTO> likeDTOs = likeService.getLikesForPost(post.getId());
                                          List<CommentDTO> commentDTOs = commentService.getCommentsForPost(post.getId())
                                                  .stream()
                                                  .map(CommentDTO::new)
                                                  .collect(Collectors.toList());
                                          return new PostDTO(post, likeDTOs, imageDTOs, commentDTOs);
                                      })
                                      .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsForPost(postId);

        List<CommentDTO> commentDTOs = comments.stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
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

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId, @RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
        }

        try {
            int updatedLikes = likeService.toggleLike(postId, userId);
            return ResponseEntity.ok(Map.of("likes", updatedLikes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO request) {
        Long userId = request.getUserId();
        String content = request.getContent();

       if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
        }

        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Content required"));
        }
        try {
            CommentDTO createdComment = commentService.addComment(postId, userId, content);
            return ResponseEntity.ok(createdComment);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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
