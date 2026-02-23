package rikko.yugen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    public Follow(User user, Artist followedArtist) {
        this.follower = user;
        this.followee = followedArtist;
        this.id = new FollowId(user.getId(), followedArtist.getId());
    }

    @EmbeddedId
    private FollowId id;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @MapsId("followeeId")
    @JoinColumn(name = "followee_id")
    private Artist followee;

    private LocalDateTime followedAt = LocalDateTime.now();


}