package rikko.yugen.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.dto.comment.CommentDTO;

import rikko.yugen.dto.comment.CommentUpdateDTO;
import rikko.yugen.service.CommentLikeService;
import rikko.yugen.service.CommentService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        CommentDTO createdComment = commentService.createComment(commentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO commentUpdateDTO) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdateDTO));
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long commentId) {
       return ResponseEntity.ok(commentLikeService.toggleLikeAndReturnResponse(commentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByUserId(@PathVariable Long userId,
                                                                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId, pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<CommentDTO>> getCurrentUserComments(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(commentService.getCurrentUserComments(pageable));
    }


}