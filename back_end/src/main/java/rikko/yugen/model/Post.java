package rikko.yugen.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name= "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    public Post(String content, LocalDateTime createdAt, Artist artist, Product product) {
        this.content = content;
        this.createdAt = createdAt;
        this.artist = artist;
        this.product = product;
    }

    public Post(String content, LocalDateTime createdAt, Artist artist) {
        this.content = content;
        this.createdAt = createdAt;
        this.artist = artist;
        this.product = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments = new HashSet<>();
    
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Image> images = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

}
