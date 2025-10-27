package rikko.yugen.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping("/me/following")
    public ResponseEntity<List<FollowWithUserDTO>> getFolloweesForCurrentUser() {
        List<FollowWithUserDTO> following = followService.getFollowingForCurrentUser();
        return ResponseEntity.ok(following);
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
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(createdUser));
    }

    @PatchMapping(value = "update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable long id,
            @RequestPart("patch") UserUpdateDTO userUpdateDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        User updatedUser = userService.updateUser(id, userUpdateDTO, file);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    @DeleteMapping(value = "/delete/{id}")
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
}