package rikko.yugen.service;

import org.springframework.stereotype.Service;
import java.util.List;

import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;

import rikko.yugen.dto.follow.FollowDTO;
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

    public List<FollowDTO> getAllFollowing(Long userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(f -> new FollowDTO(
                        f.getFollower().getId(),
                        f.getFollowee().getId(),
                        f.getFollowedAt()
                ))
                .toList();
    }

    public List<FollowDTO> getFollowersForArtist(Long artistId) {
        return followRepository.findByFolloweeId(artistId).stream()
                .map(f -> new FollowDTO(
                        f.getFollower().getId(),
                        f.getFollowee().getId(),
                        f.getFollowedAt()
                ))
                .toList();
    }

    public FollowDTO followArtist(Long userId, Long artistId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Artist followee = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found: " + artistId));

        if (follower.getId().equals(followee.getUser().getId())) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        FollowId followId = new FollowId(follower.getId(), followee.getId());
        if (followRepository.existsById(followId)) {
            // return a DTO if already following
            return new FollowDTO(followId.getFollowerId(), followId.getFolloweeId(), LocalDateTime.now());
        }

        Follow follow = new Follow(followId, follower, followee, LocalDateTime.now());
        Follow saved = followRepository.save(follow);

        return new FollowDTO(saved.getFollower().getId(), saved.getFollowee().getId(), saved.getFollowedAt());
    }

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