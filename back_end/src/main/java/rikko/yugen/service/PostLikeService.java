package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.like.PostLikeDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Post;
import rikko.yugen.model.PostLike;
import rikko.yugen.model.User;
import rikko.yugen.repository.PostLikeRepository;
import rikko.yugen.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CurrentUserHelper currentUserHelper;

    // read
    public Set<PostLikeDTO> getLikesForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return postLikeRepository.findByPost(post)
                .stream()
                .map(PostLikeDTO::new)
                .collect(Collectors.toSet());
    }

    public int getLikeCountForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return postLikeRepository.countByPost(post);
    }

    // write

    @Transactional
    public int toggleLike(Long postId) {
        User currentUser = currentUserHelper.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> existingLike = postLikeRepository
                .findByUserAndPost(currentUser, post);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
        } else {
            PostLike like = new PostLike();
            like.setUser(currentUser);
            like.setPost(post);
            postLikeRepository.save(like);
        }

        return getLikeCountForPost(postId);
    }
}