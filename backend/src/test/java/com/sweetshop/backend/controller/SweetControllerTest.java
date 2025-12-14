package com.sweetshop.backend.controller;

import com.sweetshop.backend.config.JwtAuthenticationFilter; // Import if needed (usually auto-scanned)
import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.service.CustomUserDetailsService; // Add Import
import com.sweetshop.backend.service.JwtService; // Add Import
import com.sweetshop.backend.service.SweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SweetController.class)
@AutoConfigureMockMvc(addFilters = false) // Security is disabled for requests, but beans are still created
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SweetService sweetService;

    // --- ADD THESE TWO MOCKS ---
    // These are required because JwtAuthenticationFilter is loaded by @WebMvcTest
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    // ---------------------------

    @Test
    void addSweet_shouldReturnCreatedSweet() throws Exception {
        Sweet sweet = new Sweet("Laddu", "Trad", 10.0, 50,"");
        when(sweetService.addSweet(any(Sweet.class))).thenReturn(sweet);

        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sweet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laddu"));
    }

    @Test
    void getAllSweets_shouldReturnList() throws Exception {
        List<Sweet> sweets = Arrays.asList(new Sweet("S1", "C1", 10.0, 10,""));
        when(sweetService.getAllSweets()).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void deleteSweet_shouldReturnNoContent() throws Exception {
        doNothing().when(sweetService).deleteSweet(1L);

        mockMvc.perform(delete("/api/sweets/1"))
                .andExpect(status().isNoContent());
    }
}