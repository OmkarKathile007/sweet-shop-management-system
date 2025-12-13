package com.sweetshop.backend.service;

import com.sweetshop.backend.dto.OrderRequest;
import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.repository.OrderRepository;
import com.sweetshop.backend.repository.SweetRepository;
import com.sweetshop.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SweetRepository sweetRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, SweetRepository sweetRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.sweetRepository = sweetRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order placeOrder(String username, OrderRequest request) {
        return null; // Force test failure
    }
}