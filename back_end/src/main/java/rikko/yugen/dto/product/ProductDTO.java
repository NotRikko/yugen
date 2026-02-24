package rikko.yugen.dto.product;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.collection.CollectionDTO;
import rikko.yugen.dto.image.ImageDTO;
import rikko.yugen.dto.series.SeriesDTO;
import rikko.yugen.model.Collection;
import rikko.yugen.model.Image;
import rikko.yugen.model.Product;
import rikko.yugen.model.Series;

public record ProductDTO(
        Long id,
        String name,
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
                product.getPrice(),
                product.getArtist() != null ? product.getArtist().getId() : null,
                product.getSeries() != null ? product.getSeries().stream().map(Series::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getCollections() != null ? product.getCollections().stream().map(Collection::getId).collect(Collectors.toSet()) : Collections.emptySet(),
                product.getImages() != null ? product.getImages().stream().map(Image::getUrl).collect(Collectors.toSet()) : Collections.emptySet()
        );
    }
}