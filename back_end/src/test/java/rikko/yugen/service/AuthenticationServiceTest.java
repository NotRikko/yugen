package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rikko.yugen.dto.user.UserLoginDTO;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserLoginDTO validLogin;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validLogin = new UserLoginDTO();
        validLogin.setUsername("testuser");
        validLogin.setPassword("password");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("hashedpassword");
    }

    // Authenticate if valid test

    @Test
    void authenticate_shouldReturnUser_whenCredentialsValid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        User result = authenticationService.authenticate(validLogin);

        assertNotNull(result);
        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getUsername(), result.getUsername());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    // Invalid credentials test

    @Test
    void authenticate_shouldThrowBadCredentialsException_whenCredentialsInvalid() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.authenticate(validLogin)
        );

        assertEquals("Invalid username or password", ex.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, never()).findByUsername(any());
    }

    // User does not exist test

    @Test
    void authenticate_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> authenticationService.authenticate(validLogin)
        );

        assertEquals("User not found", ex.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    // Input null test

    @Test
    void authenticate_shouldThrowIllegalArgumentException_whenUsernameOrPasswordNull() {
        UserLoginDTO nullUsername = new UserLoginDTO();
        nullUsername.setUsername(null);
        nullUsername.setPassword("pass");

        UserLoginDTO nullPassword = new UserLoginDTO();
        nullPassword.setUsername("user");
        nullPassword.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticate(nullUsername));
        assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticate(nullPassword));
        verify(authenticationManager, never()).authenticate(any());
        verify(userRepository, never()).findByUsername(any());
    }
}