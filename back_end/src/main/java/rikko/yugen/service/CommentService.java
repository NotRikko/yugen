package rikko.yugen.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Comment;
import rikko.yugen.model.User;
import rikko.yugen.model.Post;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final CurrentUserHelper currentUserHelper;

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentDTO::new)
                .toList();
    }

    public List<CommentDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(CommentDTO::new)
                .toList();
    }


    @Transactional
    public CommentDTO createComment(CommentCreateDTO dto) {
        User currentUser = currentUserHelper.getCurrentUser();

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID " + dto.getPostId()));

        Comment comment = new Comment();
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);

        return new CommentDTO(saved);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}