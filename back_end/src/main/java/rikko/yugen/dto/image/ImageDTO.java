package rikko.yugen.dto.image;

import rikko.yugen.model.Image;
public record ImageDTO(
        Long id,
        String url
) {
    public ImageDTO(Image image) {
        this(
                image.getId(),
                image.getUrl()
        );
    }
}
