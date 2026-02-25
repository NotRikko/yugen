package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.like.PostLikeDTO;
import rikko.yugen.dto.like.PostLikeResponse;
import rikko.yugen.exception.ResourceNotFoundException;
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

    // Read

    public Set<PostLikeDTO> getLikesForPost(Long postId) {
        Post post = getPostOrThrow(postId);

        return postLikeRepository.findByPost(post)
                .stream()
                .map(PostLikeDTO::new)
                .collect(Collectors.toSet());
    }

    public int getLikeCountForPost(Long postId) {
        Post post = getPostOrThrow(postId);
        return postLikeRepository.countByPost(post);
    }


    // Write

    @Transactional
    public PostLikeResponse toggleLikeAndReturnResponse(Long postId) {
        User currentUser = currentUserHelper.getCurrentUser();
        Post post = getPostOrThrow(postId);

        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(currentUser, post);

        boolean likedNow;
        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            likedNow = false;
        } else {
            PostLike like = new PostLike();
            like.setUser(currentUser);
            like.setPost(post);
            postLikeRepository.save(like);
            likedNow = true;
        }

        int updatedCount = getLikeCountForPost(postId);

        return new PostLikeResponse(updatedCount, likedNow);
    }

    // Helpers

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }
}