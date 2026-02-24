package rikko.yugen.service;

import lombok.RequiredArgsConstructor;
import rikko.yugen.dto.user.UserLoginDTO;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public User authenticate(UserLoginDTO input) {
        try {
            String normalizedUsername = input.getUsername().toLowerCase().trim();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                           normalizedUsername,
                            input.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new IllegalStateException("User should exist after successful authentication"));
    }
}