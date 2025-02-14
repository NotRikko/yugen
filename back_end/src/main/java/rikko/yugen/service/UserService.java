package rikko.yugen.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.dto.UserCreateDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

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
        // Check if username already exists
        userRepository.findByUsername(userCreateDTO.getUsername())
            .ifPresent(existingUser -> {
                throw new RuntimeException("User with username '" + existingUser.getUsername() + "' already exists.");
            });

        // Hash password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        // Create and save user
        User user = new User();
        user.setPassword(hashedPassword);
        user.setUsername(userCreateDTO.getUsername());
        user.setDisplayName(userCreateDTO.getDisplayName());
        user.setEmail(userCreateDTO.getEmail());
        user.setImage(userCreateDTO.getImage());
        user.setIsArtist(userCreateDTO.getIsArtist());

        User savedUser = userRepository.save(user);

        // If user is an artist, create an artist profile
        if (userCreateDTO.getIsArtist()) {
            Artist artist = new Artist();
            artist.setArtistName(userCreateDTO.getDisplayName()); 
            artist.setUser(savedUser);
            artistRepository.save(artist);
        }

        return savedUser;
    }
}
