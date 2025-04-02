package rikko.yugen.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.model.Like;
import rikko.yugen.model.User;
import rikko.yugen.repository.LikeRepository;
import rikko.yugen.repository.UserRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

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
    public int toggleLike(Long postId, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        Optional<Like> existingLike = likeRepository.findByUserIdAndContentIdAndContentType(userId, postId, "POST");

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());  // Unlike
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setContentId(postId);
            like.setContentType("POST");
            likeRepository.save(like);  // Like
        }

        return getLikeCountForPost(postId);  // Return updated count
    }
}
