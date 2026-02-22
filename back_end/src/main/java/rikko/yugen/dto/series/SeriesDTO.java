package rikko.yugen.dto.series;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;

import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Series;


public record SeriesDTO(
        Long id,
        String name,
        String image,
        String description,
        Set<ProductDTO> products
) {
    public SeriesDTO(Series series) {
        this(
                series.getId(),
                series.getName(),
                series.getImage(),
                series.getDescription(),
                series.getProducts() != null
                        ? series.getProducts().stream().map(ProductDTO::new).collect(Collectors.toSet())
                        : Collections.emptySet()
        );
    }
}