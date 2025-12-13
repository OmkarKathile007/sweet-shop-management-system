package com.sweetshop.backend.controller;

import com.sweetshop.backend.config.JwtAuthenticationFilter;
import com.sweetshop.backend.dto.OrderRequest;
import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.service.CustomUserDetailsService;
import com.sweetshop.backend.service.JwtService;
import com.sweetshop.backend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    // Mock Security Beans to prevent Context Load Failure
    @MockBean private JwtService jwtService;
    @MockBean private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "customer") // Simulate logged-in user
    void placeOrder_shouldReturnCreatedOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setItems(Collections.emptyList());

        Order createdOrder = new Order();
        createdOrder.setId(101L);

        // We expect the controller to pass "customer" (from @WithMockUser) to the service
        when(orderService.placeOrder(eq("customer"), any(OrderRequest.class)))
                .thenReturn(createdOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}