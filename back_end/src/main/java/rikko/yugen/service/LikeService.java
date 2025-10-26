package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Like;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.repository.LikeRepository;
import rikko.yugen.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    private final CurrentUserHelper currentUserHelper;

    public Set<LikeDTO> getLikesForPost(Long postId) {
        Set<Like> likes = likeRepository.findByContentIdAndContentType(postId, "POST");
        return likes.stream()
                .map(LikeDTO::new)
                .collect(Collectors.toSet());
    }

    public int getLikeCountForPost(Long postId) {
        return likeRepository.countByContentIdAndContentType(postId, "POST");
    }

    @Transactional
    public int toggleLike(Long postId) {
        User currentUser = currentUserHelper.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existingLike = likeRepository
                .findByUserIdAndContentIdAndContentType(currentUser.getId(), postId, "POST");

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = new Like();
            like.setUser(currentUser);
            like.setContentId(postId);
            like.setContentType("POST");
            likeRepository.save(like);
        }

        return getLikeCountForPost(postId);
    }
}