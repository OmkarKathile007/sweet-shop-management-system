package com.sweetshop.backend.controller;

import com.sweetshop.backend.dto.OrderRequest;
import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request, Authentication authentication) {
        return ResponseEntity.notFound().build(); // Stub to force failure
    }
}