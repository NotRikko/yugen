package rikko.yugen.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import rikko.yugen.exception.MultipleFieldValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;

import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserUpdateDTO;

import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.model.Image;

import rikko.yugen.exception.UserAlreadyExistsException;
import rikko.yugen.exception.EmailAlreadyExistsException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public User getUserByDisplayName(String displayName) {
        return userRepository.findByDisplayName(displayName)
            .orElseThrow(() -> new RuntimeException("User not found with name" + displayName));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username" + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        Map<String, String> errors = new HashMap<>();

        userRepository.findByUsername(userCreateDTO.getUsername())
                .ifPresent(u -> errors.put("username", "User with username '" + u.getUsername() + "' already exists."));

        userRepository.findByEmail(userCreateDTO.getEmail())
                .ifPresent(u -> errors.put("email", "User with email '" + u.getEmail() + "' already exists."));

        if (!errors.isEmpty()) {
            throw new MultipleFieldValidationException(errors);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setDisplayName(userCreateDTO.getDisplayName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setIsArtist(userCreateDTO.getIsArtist());

        User savedUser = userRepository.save(user);

        if (userCreateDTO.getIsArtist()) {
            Artist artist = new Artist();
            artist.setArtistName(userCreateDTO.getDisplayName());
            artist.setUser(savedUser);
            artistRepository.save(artist);
        }

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, UserUpdateDTO userUpdateDTO, MultipartFile profileImageFile) {
        User existingUser = getUserById(id);
        boolean isUpdated = false;

        // Update username
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(userUpdateDTO.getUsername())) {
                throw new UserAlreadyExistsException("Username is already taken");
            }
            existingUser.setUsername(userUpdateDTO.getUsername());
            isUpdated = true;
        }

        // Update displayName
        if (userUpdateDTO.getDisplayName() != null && !userUpdateDTO.getDisplayName().equals(existingUser.getDisplayName())) {
            existingUser.setDisplayName(userUpdateDTO.getDisplayName());
            isUpdated = true;
        }

        // Update email
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken");
            }
            existingUser.setEmail(userUpdateDTO.getEmail());
            isUpdated = true;
        }

        // Handle profile picture
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                String uploadedUrl = cloudinaryService.uploadImage(profileImageFile);

                Image profileImage = existingUser.getImage();

                if (profileImage != null) {
                    // Delete old image from cloudinary
                    cloudinaryService.deleteImage(profileImage.getUrl());
                    profileImage.setUrl(uploadedUrl);
                } else {
                    profileImage = new Image();
                    profileImage.setUrl(uploadedUrl);
                    profileImage.setUser(existingUser);
                    existingUser.setImage(profileImage);
                }

                isUpdated = true;

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload profile picture", e);
            }
        }

        if (isUpdated) {
            return userRepository.save(existingUser);
        }

        return existingUser;
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
