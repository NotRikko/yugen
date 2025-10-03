package rikko.yugen.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Float price;
    private Integer quantityInStock;
    private String image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Post> posts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToMany
    @JoinTable(
        name = "product_series",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "series_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Series> series;

    @ManyToMany
    @JoinTable(
        name = "product_collection",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Collection> collections;

}
