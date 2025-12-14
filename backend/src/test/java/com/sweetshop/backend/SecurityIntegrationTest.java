package com.sweetshop.backend;

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

    @Test
    void publicEndpoints_shouldReturn200() throws Exception {
        // GET /api/sweets was configured as public in SecurityConfig.
        // It should return 200 OK without any auth token.
        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoints_shouldReturn403_withoutToken() throws Exception {
        // POST /api/sweets (Adding data) is configured to require authentication.
        // Accessing it without a token MUST return 403 Forbidden.
        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\",\"price\":10.0,\"quantity\":10}")) // Dummy content
                .andExpect(status().isForbidden());
    }

}