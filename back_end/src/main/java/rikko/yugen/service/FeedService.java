package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Post;
import rikko.yugen.model.Like;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.like.LikeDTO;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.LikeRepository;
import rikko.yugen.repository.FollowRepository;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    private final CurrentUserHelper currentUserHelper;

    @Transactional(readOnly = true)
    public List<PostDTO> getGlobalFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAll(pageable).getContent();

        return mapPostsWithLikes(posts);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getUserFeed(Long userId, int page, int size) {
        List<Long> followedArtistIds = followRepository.findByFollowerId(userId)
                .stream().map(f -> f.getFollowee().getId()).toList();

        if (followedArtistIds.isEmpty()) return List.of();

        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findByArtist_IdIn(followedArtistIds, pageable);

        return mapPostsWithLikes(posts);
    }

    private List<PostDTO> mapPostsWithLikes(List<Post> posts) {
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        List<Like> likes = likeRepository.findByContentTypeAndContentIdIn("POST",postIds);

        Map<Long, Set<LikeDTO>> likesByPost = likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getContentId,
                        Collectors.mapping(LikeDTO::new, Collectors.toSet())
                ));

        return posts.stream()
                .map(post -> PostDTO.fromPost(post, likesByPost.getOrDefault(post.getId(), new HashSet<>())))
                .toList();
    }
}