package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rikko.yugen.dto.user.UserCreateDTO;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    //mock users
    private User mockUser;
    private User mockUser2;

    @BeforeEach

    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");
        mockUser.setDisplayName("Rikko");
        mockUser.setEmail("rikko@test.com");

        mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setUsername("Rikko2");
        mockUser2.setDisplayName("Rikko2");
        mockUser2.setEmail("rikko2@test.com");
    }

    //get by id tests

    @Test
    void getUserById_shouldReturnUserDTO_whenUserExists() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.username());
        assertEquals("Rikko", result.displayName());
        assertEquals("rikko@test.com", result.email());
        assertNull(result.image());
        assertEquals(false, result.isArtist());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        Long userId = 99999L;
        when(userRepository.findById(userId ))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId );

    }

    //get by username tests
    @Test
    void getUserByUsername_shouldReturnUserDTO_whenUserExists() {
        when(userRepository.findByUsername("Rikko"))
                .thenReturn(Optional.of(mockUser));

        UserDTO result = userService.getUserByUsername("Rikko");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.username());
        assertEquals("Rikko", result.displayName());
        assertEquals("rikko@test.com", result.email());
        assertNull(result.image());
        assertEquals(false, result.isArtist());
    }

    @Test
    void getUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("NotExister"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("NotExister"));
        verify(userRepository).findByUsername("NotExister");
    }

    //get by display name tests
    @Test
    void getUserByDisplayName_shouldReturnUserDTO_whenUserExists() {
        when(userRepository.findByDisplayName("Rikko"))
                .thenReturn(Optional.of(mockUser));

        UserDTO result = userService.getUserByDisplayName("Rikko");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.username());
        assertEquals("Rikko", result.displayName());
        assertEquals("rikko@test.com", result.email());
        assertNull(result.image());
        assertEquals(false, result.isArtist());
    }

    @Test
    void getUserByDisplayName_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByDisplayName("NotExister"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByDisplayName("NotExister"));
        verify(userRepository).findByDisplayName("NotExister");
    }

    //get by email tests
    @Test
    void getUserByEmail_shouldReturnUserDTO_whenUserExists() {
        when(userRepository.findByEmail("rikko@test.com"))
                .thenReturn(Optional.of(mockUser));

        UserDTO result = userService.getUserByEmail("rikko@test.com");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.username());
        assertEquals("Rikko", result.displayName());
        assertEquals("rikko@test.com", result.email());
        assertNull(result.image());
        assertEquals(false, result.isArtist());
        verify(userRepository).findByEmail("rikko@test.com");
    }

    @Test
    void getUserByEmail_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByEmail("NotExister@test.com"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("NotExister@test.com"));
        verify(userRepository).findByEmail("NotExister@test.com");
    }

    //get all users tests
    @Test
    void getAllUsers_shouldReturnListOfUserDTO_whenUsersExist() {
        when(userRepository.findAll())
                .thenReturn(List.of(mockUser, mockUser2));

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Rikko", result.get(0).username());
        assertEquals("Rikko", result.get(0).displayName());
        assertEquals("rikko@test.com", result.get(0).email());

        assertEquals("Rikko2", result.get(1).username());
        assertEquals("Rikko2", result.get(1).displayName());
        assertEquals("rikko2@test.com", result.get(1).email());

        verify(userRepository).findAll();
    }

    //create user tests

    @Test
    void createUser_shouldReturnUserDTO_whenUserCreated() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("Rikko3");
        dto.setDisplayName("Rikko3");
        dto.setEmail("rikko3@test.com");
        dto.setPassword("plainPassword");
        dto.setIsArtist(false);

        when(userRepository.existsByUsername("Rikko3")).thenReturn(false);
        when(userRepository.existsByEmail("rikko3@test.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setUsername("Rikko3");
        savedUser.setDisplayName("Rikko3");
        savedUser.setEmail("rikko3@test.com");
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(3L, result.id());
        assertEquals("Rikko3", result.username());
        assertEquals("Rikko3", result.displayName());
        assertEquals("rikko3@test.com", result.email());

        verify(userRepository).existsByUsername("Rikko3");
        verify(userRepository).existsByEmail("rikko3@test.com");
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    //update user tests
    void updateUser_shouldReturnUserDTO_whenUserUpdated() {

    }


}
