package rikko.yugen.model;

import java.util.Set;

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
    private Integer quantity_in_stock;
    private String image;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToMany
    @JoinTable(
        name = "product_series",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "series_id")
    )
    private Set<Series> series;

    @ManyToMany
    @JoinTable(
        name = "product_collection",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    private Set<Collection> collections;

}
