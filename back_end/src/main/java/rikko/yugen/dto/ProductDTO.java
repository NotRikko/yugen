package rikko.yugen.dto;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.model.Product;

public class ProductDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final Float price;
    private final Integer quantity_in_stock;
    private final String image;
    private final ArtistDTO artist;
    private final Set<SeriesDTO> series;
    private final Set<CollectionDTO> collections;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity_in_stock = product.getQuantity_in_stock();
        this.image = product.getImage();
        this.artist = product.getArtist() != null ? new ArtistDTO(product.getArtist()) : null;
        this.series = product.getSeries() != null
                    ? product.getSeries().stream()
                        .map(SeriesDTO::new)
                        .collect(Collectors.toSet())
                    : Collections.emptySet();

        this.collections = product.getCollections() != null
                    ? product.getCollections().stream()
                    .map(CollectionDTO::new)
                    .collect(Collectors.toSet())
                    : Collections.emptySet();
    }

    //Getters
    public Long getId() {
    return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getQuantityInStock() {
        return quantity_in_stock;
    }

    public String getImage() {
        return image;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public Set<SeriesDTO> getSeries() {
        return series;
    }

    public Set<CollectionDTO> getCollections() {
        return collections;
    }
}
