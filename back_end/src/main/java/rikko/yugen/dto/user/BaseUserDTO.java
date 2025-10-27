package rikko.yugen.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseUserDTO {
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private String image;
    private Boolean isArtist;

}