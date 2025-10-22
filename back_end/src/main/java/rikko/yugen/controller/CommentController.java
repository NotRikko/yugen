package rikko.yugen.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.dto.comment.CommentDTO;

import rikko.yugen.service.CommentService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        CommentDTO createdComment = commentService.createComment(commentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    /*
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId, @Req)
     */
}