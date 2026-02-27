package rikko.yugen.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artists")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends BaseModel {

    public Artist(String artistName, String bio, User user, Image profileImage, Image bannerImage) {
        this.artistName = artistName;
        this.bio = bio;
        this.user = user;

        if (profileImage != null) {
            this.setProfileImage(profileImage);
            profileImage.setProfileForArtist(this);
        }

        if (bannerImage != null) {
            this.setBannerImage(bannerImage);
            bannerImage.setBannerForArtist(this);
        }
    }

    @Column(name = "artist_name")
    private String artistName;

    private String bio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id", unique = true)
    private Image profileImage;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "banner_image_id", unique = true)
    private Image bannerImage;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> products = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Follow> followers = new HashSet<>();
}