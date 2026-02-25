package rikko.yugen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.dto.comment.CommentUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Comment;
import rikko.yugen.model.User;
import rikko.yugen.model.Post;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CurrentUserHelper currentUserHelper;

    // Get methods

    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByPostId(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        if (comments.isEmpty() && !postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }

        return comments.map(CommentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByUserId(Long userId, Pageable pageable) {
      if (!userRepository.existsById(userId)) {
          throw new ResourceNotFoundException("User", "id", userId);
      }
      return commentRepository.findByUserId(userId, pageable)
              .map(CommentDTO::new);
    }
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCurrentUserComments(Pageable pageable) {
        User currentUser = currentUserHelper.getCurrentUser();
        return commentRepository.findByUserId(currentUser.getId(), PageRequest.of(0, 10))
                .map(CommentDTO::new);
    }

    // Create methods

    @Transactional
    public CommentDTO createComment(CommentCreateDTO dto) {
        User currentUser = currentUserHelper.getCurrentUser();

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", dto.getPostId()));

        Comment comment = new Comment();
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);

        return new CommentDTO(saved);
    }

    // Update methods

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentUpdateDTO dto) {
        User currentUser = currentUserHelper.getCurrentUser();
        Comment comment = getCommentOrThrow(commentId);

        checkCommentOwnership(comment, currentUser);

        comment.setContent(dto.getContent());
        return new CommentDTO(commentRepository.save(comment));
    }
    // Delete methods

    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = currentUserHelper.getCurrentUser();
        Comment comment = getCommentOrThrow(commentId);

        checkCommentOwnership(comment, currentUser);

        commentRepository.delete(comment);
    }

    // Helpers
    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

    private void checkCommentOwnership(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to modify this comment");
        }
    }
}