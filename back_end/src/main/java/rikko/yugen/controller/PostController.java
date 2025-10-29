package rikko.yugen.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;

import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.service.PostService;
import rikko.yugen.service.LikeService;
import rikko.yugen.service.CommentService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    @GetMapping("/")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDTO> createPost(
            @RequestPart("post") PostCreateDTO postCreateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        PostDTO postDTO = postService.createPost(postCreateDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Integer>> toggleLike(@PathVariable Long postId) {
        int updatedLikes = likeService.toggleLike(postId);
        return ResponseEntity.ok(Map.of("likes", updatedLikes));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateDTO postUpdateDTO) {
        return ResponseEntity.ok(postService.updatePost(postId, postUpdateDTO));
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}