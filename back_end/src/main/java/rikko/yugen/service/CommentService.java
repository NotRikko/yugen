package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.model.Comment;
import rikko.yugen.model.User;
import rikko.yugen.model.Post;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.repository.PostRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public Set<CommentDTO> getCommentsForPost(Long postId) {
        Set<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentDTO::new)
                .collect(Collectors.toSet());
    }

    @Transactional
    public CommentDTO addComment(Long postId, Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        Comment saved = commentRepository.save(comment);
        return new CommentDTO(saved);
    }
}