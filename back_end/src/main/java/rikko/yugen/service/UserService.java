package rikko.yugen.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;

import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserUpdateDTO;

import rikko.yugen.model.Artist;
import rikko.yugen.model.User;

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
        userRepository.findByUsername(userCreateDTO.getUsername())
            .ifPresent(existingUser -> {
                throw new RuntimeException("User with username '" + existingUser.getUsername() + "' already exists.");
            });

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User user = new User();
        user.setPassword(hashedPassword);
        user.setUsername(userCreateDTO.getUsername());
        user.setDisplayName(userCreateDTO.getDisplayName());
        user.setEmail(userCreateDTO.getEmail());
        user.setImage(userCreateDTO.getImage());
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
    public User updateUser(Long id, UserUpdateDTO userUpdateDTO, MultipartFile file) {
        try {
            User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    
                boolean isUpdated = false;

                if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().equals(existingUser.getUsername())) {
                    if (userRepository.existsByUsername(userUpdateDTO.getUsername())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
                    }
                    existingUser.setUsername(userUpdateDTO.getUsername());
                    isUpdated = true;
                }

                if (userUpdateDTO.getDisplayName() != null && !userUpdateDTO.getDisplayName().equals(existingUser.getDisplayName())) {
                    existingUser.setDisplayName(userUpdateDTO.getDisplayName());
                    isUpdated = true;
                }
        
                if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(existingUser.getEmail())) {
                    if (userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken");
                    }
                    existingUser.setEmail(userUpdateDTO.getEmail());
                    isUpdated = true;
                }
                
        
                if (file != null && !file.isEmpty()) {
                    if (existingUser.getImage() != null) {
                        cloudinaryService.deleteImage(existingUser.getImage());
                    }
                    
                    String profileImageUrl = cloudinaryService.uploadImage(file);
                    existingUser.setImage(profileImageUrl);
                    isUpdated = true;
                }
        
                if (isUpdated) {
                    return userRepository.save(existingUser);
                }
                return existingUser;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading image", e);
        }
    }
}
