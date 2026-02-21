package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

import java.util.List;

import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.exception.*;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;

import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserUpdateDTO;

import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.model.Image;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    // JWT

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    // Mapping

    private UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    // Read

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userToUserDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return userToUserDTO(user);
    }

    public UserDTO getUserByDisplayName(String displayName) {
        User user = userRepository.findByDisplayName(displayName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "displayName", displayName));
        return userToUserDTO(user);
    }

    public UserDTO getUserByEmail (String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userToUserDTO(user);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::userToUserDTO);
    }

    // Create

    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", dto.getEmail());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setDisplayName(dto.getDisplayName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsArtist(dto.getIsArtist());

        User savedUser = userRepository.save(user);

        if (dto.getIsArtist()) {
            Artist artist = new Artist();
            artist.setArtistName(dto.getDisplayName());
            artist.setUser(savedUser);
            artistRepository.save(artist);
        }

        return userToUserDTO(savedUser);
    }

    // Update

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO dto, MultipartFile profileImageFile) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        boolean isUpdated = false;

        if (dto.getUsername() != null && !dto.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
            }
            existingUser.setUsername(dto.getUsername());
            isUpdated = true;
        }

        if (dto.getDisplayName() != null && !dto.getDisplayName().equals(existingUser.getDisplayName())) {
            existingUser.setDisplayName(dto.getDisplayName());
            isUpdated = true;
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new ResourceAlreadyExistsException("User", "email", dto.getEmail());
            }
            existingUser.setEmail(dto.getEmail());
            isUpdated = true;
        }

        if (dto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            isUpdated = true;
        }

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                String uploadedUrl = cloudinaryService.uploadImage(profileImageFile);
                Image profileImage = existingUser.getProfileImage();

                if (profileImage != null) {
                    cloudinaryService.deleteImage(profileImage.getUrl());
                    profileImage.setUrl(uploadedUrl);
                } else {
                    profileImage = new Image();
                    profileImage.setUrl(uploadedUrl);
                    profileImage.setUser(existingUser);
                    existingUser.setProfileImage(profileImage);
                }

                isUpdated = true;
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload profile image", e);
            }
        }

        if (isUpdated) {
            existingUser = userRepository.save(existingUser);
        }

        return userToUserDTO(existingUser);
    }

    // Delete

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
    }

}