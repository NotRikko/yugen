package rikko.yugen.service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import rikko.yugen.repository.UserRepository;
import rikko.yugen.model.User;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User getUserByName(String name) {
        return userRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("User not found with name" + name));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username" + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /* 
    public User createUser(User user) {
        userRepository.findByUsername(user.getUsername())
            .ifPresent(existingUser -> {
                throw new RuntimeException("User with username '" + existingUser.getUsername() + "' already exists." );
            });
        String hashedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
    */
}
