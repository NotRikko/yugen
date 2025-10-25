package rikko.yugen.dto.product;

import java.util.Set;

public class ProductCreateDTO {
    private String name;
    private String description;
    private Float price;
    private Integer quantityInStock;
    private Set<String> images;
    private Set<Long> seriesIds;
    private Set<Long> collectionIds;

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

    public Set<String> getImages() {
        return images;
    }

    public Set<Long> getSeriesIds() {
        return seriesIds;
    }

    public Set<Long> getCollectionIds() {
        return collectionIds;
    }
}