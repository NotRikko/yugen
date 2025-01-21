package rikko.yugen.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name= "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
}
