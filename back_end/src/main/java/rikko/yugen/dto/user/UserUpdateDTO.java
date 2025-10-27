package rikko.yugen.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDTO extends BaseUserDTO {
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Email(message = "Invalid email format")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName();
    }

}
