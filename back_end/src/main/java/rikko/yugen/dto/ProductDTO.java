package rikko.yugen.dto;

import rikko.yugen.model.Product;

public class ProductDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final Float price;
    private final Integer quantity_in_stock;
    private final String image;
    private final ArtistDTO artist;
    private final List<SeriesDTO> series;
    private final List<CollectionDTO> collections;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity_in_stock = product.getQuantity_in_stock();
        this.image = product.getImage();
        this.artist = product.getArtist() != null ? new ArtistDTO(product.getArtist()) : null;
        this.series = product.getSeries() != null
                    ? product.getSeries().stream().map(SeriesDTO::new).toList()
                    : null;
        this.collections = product.getCollection() != null
                    ? product.getCollection().stream().map(CollectionDTO::new).toList()
                    : null;
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

    public List<SeriesDTO> getSeries() {
        return series;
    }

    public List<CollectionDTO> getCollections() {
        return collections;
    }
}
