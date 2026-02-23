package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;

import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.repository.FollowRepository;
import rikko.yugen.repository.ArtistRepository;

import java.time.LocalDateTime;

import rikko.yugen.helpers.CurrentUserHelper;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final ArtistRepository artistRepository;
    private final CurrentUserHelper currentUserHelper;

    // Helpers

    private User getCurrentUser() {
        return currentUserHelper.getCurrentUser();
    }

    private FollowWithUserDTO mapFollowToDTO(Follow follow, boolean followerView) {
        if (followerView) {
            User follower = follow.getFollower();
            return new FollowWithUserDTO(
                    follower.getId(),
                    follower.getUsername(),
                    follower.getDisplayName(),
                    follower.getProfileImage() != null ? follower.getProfileImage().getUrl() : null,
                    follow.getFollowedAt()
            );
        } else {
            Artist followee = follow.getFollowee();
            return new FollowWithUserDTO(
                    followee.getId(),
                    followee.getUser().getUsername(),
                    followee.getArtistName(),
                    followee.getProfileImage() != null ? followee.getProfileImage().getUrl() : null,
                    follow.getFollowedAt()
            );
        }
    }

    private void validateNotSelfFollow(User user, Artist artist) {
        if (user.getId().equals(artist.getUser().getId())) {
            throw new IllegalStateException("User cannot follow themselves.");
        }
    }

    // Read

    @Transactional(readOnly = true)
    public List<FollowWithUserDTO> getAllFolloweesForUser(Long userId) {
        return followRepository.findByFollowerId(userId)
                .stream()
                .map(f -> mapFollowToDTO(f, false)) // false = followee view
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowWithUserDTO> getFollowingForCurrentUser() {
        Long currentUserId = getCurrentUser().getId();
        return followRepository.findByFollowerId(currentUserId)
                .stream()
                .map(f -> mapFollowToDTO(f, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowWithUserDTO> getFollowersForArtist(Long artistId) {
        return followRepository.findByFolloweeId(artistId)
                .stream()
                .map(f -> mapFollowToDTO(f, true)) // true = follower view
                .toList();
    }

    // Write
    @Transactional
    public FollowWithUserDTO followArtist(Long artistId) {
        User user = getCurrentUser();
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", artistId));

        validateNotSelfFollow(user, artist);

        FollowId followId = new FollowId(user.getId(), artist.getId());
        return followRepository.findById(followId)
                .map(f -> mapFollowToDTO(f, false))
                .orElseGet(() -> {
                    Follow saved = followRepository.save(new Follow(followId, user, artist, LocalDateTime.now()));
                    return mapFollowToDTO(saved, false);
                });
    }

    @Transactional
    public void unfollowArtist(Long artistId) {
        User user = getCurrentUser();
        FollowId followId = new FollowId(user.getId(), artistId);

        if (!followRepository.existsById(followId)) {
            throw new ResourceNotFoundException("Follow", "id", followId);
        }

        followRepository.deleteById(followId);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long artistId) {
        User user = getCurrentUser();
        FollowId followId = new FollowId(user.getId(), artistId);
        return followRepository.existsById(followId);
    }
}