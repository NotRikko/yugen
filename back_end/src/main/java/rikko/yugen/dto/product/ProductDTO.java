package rikko.yugen.dto.product;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.model.Collection;
import rikko.yugen.model.Image;
import rikko.yugen.model.Product;
import rikko.yugen.model.Series;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Float price,
        Long artistId,
        Set<Long> seriesIds,
        Set<Long> collectionIds,
        Set<String> imageUrls
) {
    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getArtist() != null ? product.getArtist().getId() : null,
                product.getSeries() != null ? product.getSeries().stream().map(Series::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getCollections() != null ? product.getCollections().stream().map(Collection::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getImages() != null ? product.getImages().stream().map(Image::getUrl).collect(Collectors.toSet()) : Collections.emptySet()
        );
    }
}