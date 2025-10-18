package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.model.Comment;
import rikko.yugen.model.User;
import rikko.yugen.model.Post;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

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
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID " + dto.getUserId()));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID " + dto.getPostId()));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);

        return new CommentDTO(saved);
    }
}