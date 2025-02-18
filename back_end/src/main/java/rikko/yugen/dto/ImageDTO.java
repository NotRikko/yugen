package rikko.yugen.dto;

import rikko.yugen.model.Image;
public class ImageDTO {
    private final Long id;
    private final String url;
    private final String contentType;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.url = image.getUrl();
        this.contentType = image.getContentType();
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String contentType() {
        return contentType;
    }
}
