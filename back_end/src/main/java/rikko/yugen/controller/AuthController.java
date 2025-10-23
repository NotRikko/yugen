package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.model.LoginResponse;
import rikko.yugen.model.User;

import rikko.yugen.dto.user.*;
import rikko.yugen.service.*;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        User authenticatedUser = authenticationService.authenticate(userLoginDTO);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(new LoginResponseDTO(loginResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserCreateDTO dto) {
        User createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(createdUser));
    }
}