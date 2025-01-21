package rikko.yugen.service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import rikko.yugen.repository.UserRepository;
import rikko.yugen.dto.UserCreateDTO;
import rikko.yugen.model.User;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User getUserByDisplayName(String displayName) {
        return userRepository.findByDisplayName(displayName)
            .orElseThrow(() -> new RuntimeException("User not found with name" + displayName));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username" + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

     
    public User createUser(UserCreateDTO userCreateDTO) {
        userRepository.findByUsername(userCreateDTO.getUsername())
            .ifPresent(existingUser -> {
                throw new RuntimeException("User with username '" + existingUser.getUsername() + "' already exists." );
            });

        User user = new User();
    
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        user.setPassword(hashedPassword);
        user.setUsername(userCreateDTO.getUsername());
        user.setDisplayName(userCreateDTO.getDisplayName());
        user.setEmail(userCreateDTO.getEmail());
        user.setImage(userCreateDTO.getImage());


        return userRepository.save(user);
    }
    
}
