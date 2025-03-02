package rikko.yugen.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.model.Like;
import rikko.yugen.repository.LikeRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    public Set<LikeDTO> getLikesForPost(Long postId) {
        Set<Like> likes = likeRepository.findByContentIdAndContentType(postId, "POST");
        return likes.stream()
                    .map(LikeDTO::new)
                    .collect(Collectors.toSet());
    }
}
