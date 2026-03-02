package rikko.yugen.dto.product;

import rikko.yugen.model.Product;

import java.time.LocalDateTime;

public record ProductSummaryDTO(
        Long id,
        String name,
        Float price,
        String mainImageUrl,
        Long artistId,
        LocalDateTime createdAt
) {
    public ProductSummaryDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImages() != null && !product.getImages().isEmpty()
                        ? product.getImages().iterator().next().getUrl()
                        : null,
                product.getArtist() != null ? product.getArtist().getId() : null,
                product.getCreatedAt()
        );
    }
}