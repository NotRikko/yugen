package rikko.yugen.dto.series;

import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Series;

public class SeriesDTO {
    private final Long id;
    private final String name;
    private final String image;
    private final String description;
    private final Set<ProductDTO> products;

    public SeriesDTO(Series series) {
        this.id = series.getId();
        this.name = series.getName();
        this.image = series.getImage();
        this.description = series.getDescription();
        this.products = series.getProducts() != null
                        ? series.getProducts().stream()
                                  .map(ProductDTO::new)
                                  .collect(Collectors.toSet()) 
                        : null;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }
}
