package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rikko.yugen.model.User;
import jakarta.validation.Valid;

import rikko.yugen.dto.user.*;
import rikko.yugen.service.*;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        User authenticatedUser = authenticationService.authenticate(userLoginDTO);

        String accessToken = jwtService.generateAccessToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpiration(),
                jwtService.getRefreshTokenExpiration()
        );

        return ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserCreateDTO dto) {
        User createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(createdUser));
    }
}