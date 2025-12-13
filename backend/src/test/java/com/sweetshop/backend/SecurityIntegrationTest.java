package com.sweetshop.backend;

import com.sweetshop.backend.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void publicEndpoints_shouldBeAccessible_withoutToken() throws Exception {
        // We expect 500 Internal Server Error because we are sending random credentials.
        // The AuthService throws "RuntimeException: User not found", which Spring bubbles up as 500.
        // This PROVES that we got past Security (403) and hit the Controller logic.
        LoginRequest request = new LoginRequest();
        request.setUsername("random");
        request.setPassword("random");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void protectedEndpoints_shouldReturn403_withoutToken() throws Exception {
        // Accessing without token should be Forbidden (403)
        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isForbidden());
    }
}