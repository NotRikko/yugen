package rikko.yugen.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
