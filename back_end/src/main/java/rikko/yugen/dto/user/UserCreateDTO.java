package rikko.yugen.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateDTO extends BaseUserDTO {
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @NotBlank(message = "Name is required")
    @Override
    public String getDisplayName() {
        return super.getDisplayName();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}