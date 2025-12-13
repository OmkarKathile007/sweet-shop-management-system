package com.sweetshop.backend.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_shouldReturnTokenString() {
        String token = jwtService.generateToken("testuser");
        assertNotNull(token, "Token should not be null");
        assertTrue(token.length() > 10, "Token should be a valid JWT string");
    }

    @Test
    void extractUsername_shouldReturnUsername_fromValidToken() {
        String username = "testUser";
        String token = jwtService.generateToken(username);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }
}