package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.dto.like.CommentLikeDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.*;
import rikko.yugen.repository.CommentLikeRepository;
import rikko.yugen.repository.CommentRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final CurrentUserHelper currentUserHelper;

    // Read

    public Set<CommentLikeDTO> getLikesForComment(Long commentId) {
        Comment comment = getCommentOrThrow(commentId);

        return commentLikeRepository.findByComment(comment)
                .stream()
                .map(CommentLikeDTO::new)
                .collect(Collectors.toSet());
    }

    public int getLikeCountForComment(Long commentId) {
        Comment comment = getCommentOrThrow(commentId);

        return commentLikeRepository.countByComment(comment);
    }

    // Write

    @Transactional
    public int toggleLike(Long commentId) {
        User currentUser = currentUserHelper.getCurrentUser();
        Comment comment = getCommentOrThrow(commentId);

        Optional<CommentLike> existingLike = commentLikeRepository
                .findByUserAndComment(currentUser, comment);

        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
        } else {
            CommentLike like = new CommentLike();
            like.setUser(currentUser);
            like.setComment(comment);
            commentLikeRepository.save(like);
        }

        return getLikeCountForComment(commentId);
    }

    // Helpers

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

}