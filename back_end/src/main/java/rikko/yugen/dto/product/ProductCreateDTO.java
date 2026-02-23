package rikko.yugen.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class ProductCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Float price;
    private Integer quantityInStock;
    private Set<String> images;
    private Set<Long> seriesIds;
    private Set<Long> collectionIds;

}