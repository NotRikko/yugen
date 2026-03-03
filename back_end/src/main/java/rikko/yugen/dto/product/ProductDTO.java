package rikko.yugen.dto.product;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.dto.artist.ArtistSummaryDTO;
import rikko.yugen.model.Collection;
import rikko.yugen.model.Image;
import rikko.yugen.model.Product;
import rikko.yugen.model.Series;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Float price,
        Integer quantityInStock,
        ArtistSummaryDTO artist,
        Set<Long> seriesIds,
        Set<Long> collectionIds,
        Set<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityInStock(),
                product.getArtist() != null ? new ArtistSummaryDTO(product.getArtist()) : null,
                product.getSeries() != null ? product.getSeries().stream().map(Series::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getCollections() != null ? product.getCollections().stream().map(Collection::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getImages() != null ? product.getImages().stream().map(Image::getUrl).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}