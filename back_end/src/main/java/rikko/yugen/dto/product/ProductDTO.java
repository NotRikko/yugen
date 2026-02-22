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

public record ProductDTO(
        Long id,
        String name,
        String description,
        Float price,
        Integer quantityInStock,
        Set<ImageDTO> images,
        ArtistDTO artist,
        Set<SeriesDTO> series,
        Set<CollectionDTO> collections
) {
    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityInStock(),
                product.getImages() != null
                        ? product.getImages().stream()
                        .map(ImageDTO::new)
                        .collect(Collectors.toSet())
                        :new HashSet<>(),
                product.getArtist() != null ? new ArtistDTO(product.getArtist()) : null,
                product.getSeries() != null
                        ? product.getSeries().stream().map(SeriesDTO::new).collect(Collectors.toSet())
                        : Collections.emptySet(),
                product.getCollections() != null
                        ? product.getCollections().stream().map(CollectionDTO::new).collect(Collectors.toSet())
                        : Collections.emptySet()
        );
    }
}