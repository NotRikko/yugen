package rikko.yugen.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.dto.user.*;
import rikko.yugen.model.User;
import rikko.yugen.service.*;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    // Users

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/by-display-name/{displayName}")
    public ResponseEntity<UserDTO> getUserByDisplayName(@PathVariable String displayName) {
        UserDTO userDTO = userService.getUserByDisplayName(displayName);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        UserDTO userDTO = userService.getUserById(user.getId());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestPart("patch") UserUpdateDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        UserDTO updatedUser = userService.updateUser(id, dto, file);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }

    // Follows

    @GetMapping("/me/following")
    public ResponseEntity<List<FollowWithUserDTO>> getFolloweesForCurrentUser() {
        List<FollowWithUserDTO> following = followService.getFollowingForCurrentUser();
        return ResponseEntity.ok(following);
    }
}