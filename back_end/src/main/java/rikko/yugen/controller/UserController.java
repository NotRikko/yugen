package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;

import rikko.yugen.dto.user.LoginResponseDTO;
import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.dto.user.UserLoginDTO;
import rikko.yugen.model.LoginResponse;
import rikko.yugen.model.User;
import rikko.yugen.service.UserService;
import rikko.yugen.service.JwtService;
import rikko.yugen.service.AuthenticationService;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;    

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with name:" + username);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser(@RequestHeader("Authorization") String token) {
        // Extract the token 
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Extract user information from the token
        String username = jwtService.extractUsername(jwtToken); // Implement this method in JwtService

        // Get the user from the service
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authenticated user not found");
        }

        // Return the user details as a UserDTO
        UserDTO userDTO = new UserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers()
        .stream()
        .map(UserDTO::new)
        .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO ) {
        User createdUser = userService.createUser(userCreateDTO);
        UserDTO userDTO = new UserDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        User authenticatedUser = authenticationService.authenticate(userLoginDTO);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(loginResponse);
        return ResponseEntity.ok(loginResponseDTO);
    }

    /* 
    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        User updatedUser = userService.updateUser(id, userUpdateDTO);
    
        UserDTO updatedUserDTO = new UserDTO(updatedUser);

        return ResponseEntity.ok(updatedUserDTO);
    }
    */
}
