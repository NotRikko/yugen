package rikko.yugen.model;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
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

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Set<Series> getSeries() {
        return series;
    }
    
    public void setSeries(Set<Series> series) {
        this.series = series;
    }

    public Set<Collection> getCollections() {
        return collections;
    }
    
    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Float getPrice() {
        return price;
    }
    
    public void setPrice(Float price) {
        this.price = price;
    }
    
    public Integer getQuantity_in_stock() {
        return quantity_in_stock;
    }
    
    public void setQuantity_in_stock(Integer quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Artist getArtist() {
        return artist;
    }
    
    public void setArtist(Artist artist) {
        this.artist = artist;
    } 
}
