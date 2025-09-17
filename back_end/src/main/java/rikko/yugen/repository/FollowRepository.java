package rikko.yugen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowee(User followee);
    boolean existsByFollowerAndFollowee(User follower, User followee);
    boolean existsById(FollowId followId);
    void deleteById(FollowId followId);
}
