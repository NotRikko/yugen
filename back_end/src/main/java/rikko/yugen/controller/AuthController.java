package rikko.yugen.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rikko.yugen.helpers.JwtCookieHelper;
import rikko.yugen.model.User;
import jakarta.validation.Valid;

import rikko.yugen.dto.user.*;
import rikko.yugen.service.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtCookieHelper jwtCookieHelper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(
            @RequestBody @Valid UserLoginDTO userLoginDTO,
            HttpServletResponse response
    ) {
        User authenticatedUser = authenticationService.authenticate(userLoginDTO);
        return ResponseEntity.ok(jwtService.generateLoginResponseWithRefreshToken(response, authenticatedUser));
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        jwtCookieHelper.clearRefreshToken(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(jwtService.refreshAccessToken(request));
    }
}