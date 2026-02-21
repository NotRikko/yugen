package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.*;

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

    // global feed
    @Transactional(readOnly = true)
    public FeedResponse<PostDTO> getGlobalFeed(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDTO> postDTOs = postPage.getContent()
                .stream()
                .map(PostDTO::new)
                .toList();

        return new FeedResponse<>(postDTOs, postPage.hasNext());
    }

    // user feed
    @Transactional(readOnly = true)
    public FeedResponse<PostDTO> getUserFeed(Pageable pageable) {
        User currentUser = currentUserHelper.getCurrentUser();

        List<Long> followedArtistIds = followRepository.findByFollowerId(currentUser.getId())
                .stream()
                .map(f -> f.getFollowee().getId())
                .toList();

        if (followedArtistIds.isEmpty()) {
            return new FeedResponse<>(Collections.emptyList(), false);
        }

        Page<Post> postPage = postRepository.findByArtist_IdIn(followedArtistIds, pageable);

        List<PostDTO> postDTOs = postPage.getContent()
                .stream()
                .map(PostDTO::new)
                .toList();

        return new FeedResponse<>(postDTOs, postPage.hasNext());
    }

    public record FeedResponse<T>(List<T> posts, boolean hasNext) {}
}