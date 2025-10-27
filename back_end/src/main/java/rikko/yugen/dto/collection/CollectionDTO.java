package rikko.yugen.dto.collection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;

import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Collection;

public record CollectionDTO(
        Long id,
        String name,
        String description,
        String image,
        Set<ProductDTO> products
) {
    public CollectionDTO(Collection collection) {
        this(
                collection.getId(),
                collection.getName(),
                collection.getDescription(),
                collection.getImage(),
                collection.getProducts() != null
                        ? collection.getProducts().stream()
                        .map(ProductDTO::fromProduct)
                        .collect(Collectors.toSet())
                        : Collections.emptySet()
        );
    }
}