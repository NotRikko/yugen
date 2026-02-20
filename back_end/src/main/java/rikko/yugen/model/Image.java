package rikko.yugen.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne(mappedBy = "profileImage")
    private User user;

    @OneToOne(mappedBy = "bannerImage")
    private Artist bannerForArtist;

    @OneToOne(mappedBy = "profileImage")
    private Artist profileForArtist;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}