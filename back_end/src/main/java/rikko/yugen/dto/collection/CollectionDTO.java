package rikko.yugen.dto.collection;

import java.util.Set;
import java.util.stream.Collectors;

import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Collection;

public class CollectionDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final String image;
    private final Set<ProductDTO> products;

    public CollectionDTO(Collection collection) {
        this.id = collection.getId();
        this.name = collection.getName();
        this.description = collection.getDescription();
        this.image = collection.getImage();
        this.products = collection.getProducts() != null
                        ? collection.getProducts().stream().map(ProductDTO::new).collect(Collectors.toSet())
                        : null;
    }

    //Getters
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getImage() {
        return image;
    }
    
    public Set<ProductDTO> getProducts() {
        return products;
    }
}
