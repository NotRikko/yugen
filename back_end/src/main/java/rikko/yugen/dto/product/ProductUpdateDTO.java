package rikko.yugen.dto.product;

import lombok.Getter;
import lombok.Setter;
import rikko.yugen.dto.collection.CollectionDTO;
import rikko.yugen.dto.series.SeriesDTO;

import java.util.Set;

@Setter
@Getter
public class ProductUpdateDTO {
    private String name;
    private String description;
    private Float price;
    private Integer quantityInStock;
    private Set<SeriesDTO> series;
    private Set<CollectionDTO> collections;
    private Set<Long> existingImageIds;
}