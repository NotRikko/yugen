package rikko.yugen.dto.user;

import rikko.yugen.model.LoginResponse;

public class LoginResponseDTO {
    private final String token;
    private final Long expiresIn;

    public LoginResponseDTO(LoginResponse loginResponse) {
        this.token = loginResponse.getToken();
        this.expiresIn = loginResponse.getExpiresIn();
    }

    // Getters
    public String getToken() {
        return token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
