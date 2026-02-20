package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;

import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.repository.FollowRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.repository.ArtistRepository;

import java.time.LocalDateTime;

import rikko.yugen.helpers.CurrentUserHelper;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final CurrentUserHelper currentUserHelper;

    public List<FollowWithUserDTO> getAllFolloweesForUser(Long userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(f -> new FollowWithUserDTO(
                        f.getFollowee().getId(),
                        f.getFollowee().getUser().getUsername(),
                        f.getFollowee().getArtistName(),
                        f.getFollowee().getProfileImage().getUrl(),
                        f.getFollowedAt()
                ))
                .toList();
    }

    public List<FollowWithUserDTO> getFollowingForCurrentUser() {
        User currentUser = currentUserHelper.getCurrentUser();
        return followRepository.findByFollowerId(currentUser.getId()).stream()
                .map(f -> new FollowWithUserDTO(
                        f.getFollowee().getId(),
                        f.getFollowee().getUser().getUsername(),
                        f.getFollowee().getArtistName(),
                        f.getFollowee().getProfileImage().getUrl(),
                        f.getFollowedAt()
                ))
                .toList();
    }

    public List<FollowWithUserDTO> getFollowersForArtist(Long artistId) {
        return followRepository.findByFolloweeId(artistId).stream()
                .map(f -> {
                    User follower = f.getFollower();
                    String imageUrl = (follower.getProfileImage() != null) ? follower.getProfileImage().getUrl() : null;
                    return new FollowWithUserDTO(
                            follower.getId(),
                            follower.getUsername(),
                            follower.getDisplayName(),
                            imageUrl,
                            f.getFollowedAt()
                    );
                })
                .toList();
    }

    @Transactional
    public FollowWithUserDTO followArtist(Long artistId) {
        User currentUser = currentUserHelper.getCurrentUser();
        Artist followee = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found: " + artistId));

        if (currentUser.getId().equals(followee.getUser().getId())) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        FollowId followId = new FollowId(currentUser.getId(), followee.getId());
        if (followRepository.existsById(followId)) {
            Follow existing = followRepository.findById(followId).get();
            return new FollowWithUserDTO(
                    existing.getFollowee().getId(),
                    existing.getFollowee().getUser().getUsername(),
                    existing.getFollowee().getArtistName(),
                    existing.getFollowee().getProfileImage().getUrl(),
                    existing.getFollowedAt()
            );
        }

        Follow follow = new Follow(followId, currentUser, followee, LocalDateTime.now());
        Follow saved = followRepository.save(follow);

        return new FollowWithUserDTO(
                saved.getFollowee().getId(),
                saved.getFollowee().getUser().getUsername(),
                saved.getFollowee().getArtistName(),
                saved.getFollowee().getProfileImage().getUrl(),
                saved.getFollowedAt()
        );
    }

    @Transactional
    public void unfollowArtist(Long artistId) {
        User currentUser = currentUserHelper.getCurrentUser();
        FollowId followId = new FollowId(currentUser.getId(), artistId);
        if (followRepository.existsById(followId)) {
            followRepository.deleteById(followId);
        }
    }

    public boolean isFollowing(Long artistId) {
        User currentUser = currentUserHelper.getCurrentUser();
        return followRepository.existsById(new FollowId(currentUser.getId(), artistId));
    }
}