package rikko.yugen.service;

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

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    public FollowService(FollowRepository followRepository,
                         UserRepository userRepository,
                         ArtistRepository artistRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    public List<FollowWithUserDTO> getAllFolloweesForUser(Long userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(f -> new FollowWithUserDTO(
                        f.getFollowee().getId(),
                        f.getFollowee().getUser().getUsername(),
                        f.getFollowee().getArtistName(),
                        f.getFollowee().getProfilePictureUrl(),
                        f.getFollowedAt()
                ))
                .toList();
    }

    public List<FollowWithUserDTO> getFollowersForArtist(Long artistId) {
        return followRepository.findByFolloweeId(artistId).stream()
                .map(f -> {
                    User follower = f.getFollower();
                    String imageUrl = (follower.getImage() != null) ? follower.getImage().getUrl() : null;
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
    public FollowWithUserDTO followArtist(Long userId, Long artistId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Artist followee = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found: " + artistId));

        if (follower.getId().equals(followee.getUser().getId())) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        FollowId followId = new FollowId(follower.getId(), followee.getId());
        if (followRepository.existsById(followId)) {
            Follow existing = followRepository.findById(followId).get();
            return new FollowWithUserDTO(
                    existing.getFollowee().getId(),
                    existing.getFollowee().getUser().getUsername(),
                    existing.getFollowee().getArtistName(),
                    existing.getFollowee().getProfilePictureUrl(),
                    existing.getFollowedAt()
            );
        }

        Follow follow = new Follow(followId, follower, followee, LocalDateTime.now());
        Follow saved = followRepository.save(follow);

        return new FollowWithUserDTO(
                saved.getFollowee().getId(),
                saved.getFollowee().getUser().getUsername(),
                saved.getFollowee().getArtistName(),
                saved.getFollowee().getProfilePictureUrl(),
                saved.getFollowedAt()
        );
    }

    @Transactional
    public void unfollowArtist(Long userId, Long artistId) {
        FollowId followId = new FollowId(userId, artistId);
        if (followRepository.existsById(followId)) {
            followRepository.deleteById(followId);
        }
    }

    public boolean isFollowing(Long userId, Long artistId) {
        return followRepository.existsById(new FollowId(userId, artistId));
    }
}