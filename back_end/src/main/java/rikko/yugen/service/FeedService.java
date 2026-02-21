package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
    public Page<PostDTO> getGlobalFeed(int page, int size) {
        return getPostDTOS(page, size);
    }

    // user feed
    @Transactional(readOnly = true)
    public Page<PostDTO> getUserFeed(int page, int size) {
        User currentUser = currentUserHelper.getCurrentUser();
        List<Long> followedArtistIds = followRepository.findByFollowerId(currentUser.getId())
                .stream()
                .map(f -> f.getFollowee().getId())
                .toList();

        if (followedArtistIds.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByArtist_IdIn(followedArtistIds, pageable);

        List<PostDTO> postDTOs = postPage.getContent()
                .stream()
                .map(PostDTO::new)
                .toList();

        return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> getAllPosts(int page, int size) {
        return getPostDTOS(page, size);
    }

    // helper

    private Page<PostDTO> getPostDTOS(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDTO> postDTOs = postPage.getContent()
                .stream()
                .map(PostDTO::new)
                .toList();

        return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
    }
}