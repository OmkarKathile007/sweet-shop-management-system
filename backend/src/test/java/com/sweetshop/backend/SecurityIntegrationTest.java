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
        // We try to hit the login endpoint.
        // Even if the login fails (bad creds), we expect 403 or 404 or 400 from the Controller.
        // BUT if Security is blocking us, we will get 401 Unauthorized immediately.

        // Note: Since we haven't implemented a real user in the DB for this test,
        // the Controller might return 403 or throw an exception,
        // but getting past the 401 filter is what counts.
        // Actually, let's just check that we DO NOT get 401 for /api/auth/login.

        LoginRequest request = new LoginRequest();
        request.setUsername("random");
        request.setPassword("random");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(404));
        // Why 404? Because our AuthController returns 404 if user not found (exception handler not yet set global).
        // If Security blocks us, it would be 401.
    }

    @Test
    void protectedEndpoints_shouldReturn401_withoutToken() throws Exception {
        // Try to access a random protected URL
        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isUnauthorized()); // 401
    }
}