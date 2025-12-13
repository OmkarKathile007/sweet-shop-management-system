package com.sweetshop.backend.controller;

import com.sweetshop.backend.dto.RegisterRequest;
import com.sweetshop.backend.model.User;
import com.sweetshop.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Correct import for MockBean
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.sweetshop.backend.dto.LoginRequest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass security filters for this unit test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService; // Mock the service layer

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_shouldReturn200_whenValidRequest() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setRole("USER");

        User savedUser = new User("newuser", "encoded", "USER");
        savedUser.setId(1L);

        when(authService.register(anyString(), anyString(), anyString())).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // We expect 200 OK
    }
    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("validUser");
        request.setPassword("validPassword");

        String fakeToken = "fake-jwt-token";
        when(authService.login("validUser", "validPassword")).thenReturn(fakeToken);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // We expect 200 OK
    }
}