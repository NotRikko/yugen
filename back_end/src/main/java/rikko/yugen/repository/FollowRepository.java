package rikko.yugen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.model.Artist;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findByFollowerId(Long userId);
    List<Follow> findByFolloweeId(Long artistId);
    boolean existsByFollowerAndFollowee(User follower, Artist followee);
}