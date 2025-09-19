package rikko.yugen.dto.user;

import rikko.yugen.model.User;

public class UserDTO extends BaseUserDTO {
    private Long artistId;

    public UserDTO(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setDisplayName(user.getDisplayName());
        this.setEmail(user.getEmail());
        this.setImage(user.getImage() != null ? user.getImage().getUrl() : null);
        this.setIsArtist(user.getIsArtist());
        this.artistId = user.getArtist() != null ? user.getArtist().getId() : null;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
}
