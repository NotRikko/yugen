package rikko.yugen.dto.image;

import rikko.yugen.model.Image;
public class ImageDTO {
    private final Long id;
    private final String url;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

}
