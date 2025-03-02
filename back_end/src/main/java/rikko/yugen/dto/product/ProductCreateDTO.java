package rikko.yugen.dto.product;

import java.util.Set;

public class ProductCreateDTO {
    private String name;
    private String description;
    private Float price;
    private Integer quantityInStock;
    private String image;
    private Long artistId;
    private Set<Long> seriesIds;
    private Set<Long> collectionIds;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Set<Long> getSeriesIds() {
        return seriesIds;
    }

    public void setSeriesIds(Set<Long> seriesIds) {
        this.seriesIds = seriesIds;
    }

    public Set<Long> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(Set<Long> collectionIds) {
        this.collectionIds = collectionIds;
    }
}