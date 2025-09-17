package rikko.yugen.service;

import org.springframework.stereotype.Service;
import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;
import rikko.yugen.repository.FollowRepository;

import java.time.LocalDateTime;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void follow(User follower, Artist followee) {
        if (follower.equals(followee)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        FollowId followId = new FollowId(follower.getId(), followee.getId());
        if (followRepository.existsById(followId)) {
            return;
        }

        Follow follow = new Follow(followId, follower, followee, LocalDateTime.now());
        followRepository.save(follow);
    }

    public void unfollow(User follower, Artist followee) {
        FollowId followId = new FollowId(follower.getId(), followee.getId());
        followRepository.deleteById(followId);
    }

    public boolean isFollowing(User follower, Artist followee) {
        return followRepository.existsById(new FollowId(follower.getId(), followee.getId()));
    }
}