package rikko.yugen.dto.user;

import lombok.Getter;
import rikko.yugen.model.User;

@Getter
public class UserDTO extends BaseUserDTO {
    private final Long artistId;

    public UserDTO(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setDisplayName(user.getDisplayName());
        this.setEmail(user.getEmail());
        this.setImage(user.getImage() != null ? user.getImage().getUrl() : null);
        this.setIsArtist(user.getIsArtist());
        this.artistId = user.getArtist() != null ? user.getArtist().getId() : null;
    }
}
