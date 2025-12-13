package com.sweetshop.backend.service;

import com.sweetshop.backend.model.User;
import com.sweetshop.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService; // Mock the new dependency

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        // ... (Keep your existing register test code exactly as is) ...
        String rawPassword = "secretPassword";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.register("newuser", rawPassword, "USER");

        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // Arrange
        String username = "validUser";
        String password = "validPassword";
        String encodedPassword = "encodedPassword";
        String expectedToken = "jwt_token";

        User user = new User(username, encodedPassword, "USER");

        // Mock finding the user
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // Mock password match check (returns true)
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        // Mock token generation
        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        // Act
        String token = authService.login(username, password);

        // Assert
        assertEquals(expectedToken, token);
        verify(jwtService).generateToken(username);
    }
}