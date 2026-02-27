package rikko.yugen.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "images")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Image  extends BaseModel{

    @Column(nullable = false)
    private String url;

    @OneToOne(mappedBy = "profileImage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToOne(mappedBy = "bannerImage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Artist bannerForArtist;

    @OneToOne(mappedBy = "profileImage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Artist profileForArtist;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Post post;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;
}