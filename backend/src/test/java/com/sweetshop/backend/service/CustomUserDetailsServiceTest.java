package com.sweetshop.backend.service;

import com.sweetshop.backend.model.User;
import com.sweetshop.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        String username = "testuser";
        User mockUser = new User(username, "encodedPass", "USER");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }
}