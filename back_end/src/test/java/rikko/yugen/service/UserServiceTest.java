package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.dto.user.UserUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Mock users

    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Rikko");
        user.setDisplayName("Rikko");
        user.setEmail("rikko@test.com");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Rikko2");
        user2.setDisplayName("Rikko2");
        user2.setEmail("rikko2@test.com");
    }

    // Test helpers

    private UserCreateDTO createUserCreateDTO(String username, String email, String password) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(username);
        dto.setDisplayName(username);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setIsArtist(false);
        return dto;
    }

    // Get by id tests

    @Nested
    class GetUserTests {

        @Test
        void getUserById_shouldReturnDTO_whenExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            UserDTO result = userService.getUserById(1L);

            assertEquals(1L, result.id());
            assertEquals("Rikko", result.username());
            verify(userRepository).findById(1L);
        }

        @Test
        void getUserById_shouldThrow_whenNotFound() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
        }

        @Test
        void getUserByUsername_shouldReturnDTO_whenExists() {
            when(userRepository.findByUsername("Rikko")).thenReturn(Optional.of(user));
            UserDTO result = userService.getUserByUsername("Rikko");

            assertEquals("Rikko", result.username());
            verify(userRepository).findByUsername("Rikko");
        }

        @Test
        void getUserByUsername_shouldThrow_whenNotFound() {
            when(userRepository.findByUsername("NotExister")).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("NotExister"));
        }

        @Test
        void getUserByEmail_shouldReturnDTO_whenExists() {
            when(userRepository.findByEmail("rikko@test.com")).thenReturn(Optional.of(user));
            UserDTO result = userService.getUserByEmail("rikko@test.com");

            assertEquals("rikko@test.com", result.email());
            verify(userRepository).findByEmail("rikko@test.com");
        }

        @Test
        void getUserByEmail_shouldThrow_whenNotFound() {
            when(userRepository.findByEmail("NotExister@test.com")).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("NotExister@test.com"));
        }

        @Test
        void getUserByDisplayName_shouldReturnDTO_whenExists() {
            when(userRepository.findByDisplayName("Rikko")).thenReturn(Optional.of(user));
            UserDTO result = userService.getUserByDisplayName("Rikko");
            assertEquals("Rikko", result.displayName());
        }

        @Test
        void getUserByDisplayName_shouldThrow_whenNotFound() {
            when(userRepository.findByDisplayName("NotExister")).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserByDisplayName("NotExister"));
        }

        @Test
        void getAllUsers_shouldReturnPaginatedList() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> page = new PageImpl<>(List.of(user, user2));
            when(userRepository.findAll(pageable)).thenReturn(page);

            Page<UserDTO> result = userService.getAllUsers(pageable);

            assertEquals(2, result.getContent().size());
            assertEquals("Rikko2", result.getContent().get(1).username());
        }
    }

    // Create user tests

    @Nested
    class CreateUserTests {

        @Test
        void createUser_shouldReturnDTO_whenValid() {
            UserCreateDTO dto = createUserCreateDTO("Rikko3", "rikko3@test.com", "plainPassword");

            when(userRepository.existsByUsername("Rikko3")).thenReturn(false);
            when(userRepository.existsByEmail("rikko3@test.com")).thenReturn(false);
            when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

            User savedUser = new User();
            savedUser.setId(3L);
            savedUser.setUsername("Rikko3");
            savedUser.setEmail("rikko3@test.com");
            savedUser.setPassword("encodedPassword");

            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            UserDTO result = userService.createUser(dto);

            assertEquals(3L, result.id());
            assertEquals("Rikko3", result.username());

            verify(userRepository).existsByUsername("Rikko3");
            verify(userRepository).existsByEmail("rikko3@test.com");
            verify(passwordEncoder).encode("plainPassword");
            verify(userRepository).save(any(User.class));
        }
    }

    // Update user tests

    @Nested
    class UpdateUserTests {

        @Test
        void updateUser_shouldUpdateFields() {
            UserUpdateDTO dto = new UserUpdateDTO();
            dto.setDisplayName("New Name");

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

            UserDTO result = userService.updateUser(1L, dto, null);

            assertEquals("New Name", result.displayName());
            verify(userRepository).save(user);
        }

        @Test
        void updateUser_shouldThrow_whenNotFound() {
            UserUpdateDTO dto = new UserUpdateDTO();
            dto.setDisplayName("New Name");
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(999L, dto, null));
        }
    }



    // Delete user tests

    @Nested
    class DeleteUserTests {

        @Test
        void shouldDeleteUser_whenUserExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            userService.deleteUser(1L);

            verify(userRepository).delete(user);
        }

        @Test
        void shouldThrowException_whenUserDoesNotExist() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));

            verify(userRepository, never()).delete(any());
        }
    }


}
