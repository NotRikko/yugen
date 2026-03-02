package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.*;

import rikko.yugen.dto.post.FeedPostDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Post;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.model.User;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.FollowRepository;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final CurrentUserHelper currentUserHelper;

    // Helpers

    private List<Long> getFollowedArtistIds(User user) {
        return followRepository.findByFollowerId(user.getId())
                .stream()
                .map(f -> f.getFollowee().getId())
                .toList();
    }

    // Global feed

    @Transactional(readOnly = true)
    public FeedResponse<FeedPostDTO> getGlobalFeed(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);

        List<FeedPostDTO> dtoList = postPage.stream()
                .map(FeedPostDTO::new)
                .toList();

        return new FeedResponse<>(dtoList, postPage.hasNext());
    }

    // User feed

    @Transactional(readOnly = true)
    public FeedResponse<FeedPostDTO> getUserFeed(Pageable pageable) {
        User currentUser = currentUserHelper.getCurrentUser();
        List<Long> followedArtistIds = getFollowedArtistIds(currentUser);

        if (followedArtistIds.isEmpty()) {
            return new FeedResponse<>(Collections.emptyList(), false);
        }

        Page<Post> postPage = postRepository.findByArtist_IdIn(followedArtistIds, pageable);

        List<FeedPostDTO> dtoList = postPage.stream()
                .map(FeedPostDTO::new)
                .toList();

        return new FeedResponse<>(dtoList, postPage.hasNext());
    }

    public record FeedResponse<T>(List<T> posts, boolean hasNext) {}
}