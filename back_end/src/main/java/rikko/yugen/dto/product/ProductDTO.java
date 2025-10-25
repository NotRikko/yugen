package rikko.yugen.dto.product;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.collection.CollectionDTO;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.series.SeriesDTO;
import rikko.yugen.model.Product;

public class ProductDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final Float price;
    private final Integer quantityInStock;
    private final Set<ImageDTO> images;
    private final ArtistDTO artist;
    private final Set<SeriesDTO> series;
    private final Set<CollectionDTO> collections;

    public ProductDTO(Product product, Set<ImageDTO> images) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantityInStock = product.getQuantityInStock();
        this.images = images != null ? images : new HashSet<>();
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

    public static ProductDTO fromProduct(Product product) {
        Set<ImageDTO> imageDTOs = product.getImages() != null
                ? product.getImages().stream().map(ImageDTO::new).collect(Collectors.toSet())
                : new HashSet<>();

        return new ProductDTO(product, imageDTOs);
    }

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
        return quantityInStock;
    }

    public Set<ImageDTO> getImages() {
        return images;
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