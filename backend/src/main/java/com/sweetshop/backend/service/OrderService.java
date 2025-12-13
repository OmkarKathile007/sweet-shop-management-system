package com.sweetshop.backend.service;

import com.sweetshop.backend.dto.OrderRequest;
import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.repository.OrderRepository;
import com.sweetshop.backend.repository.SweetRepository;
import com.sweetshop.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sweetshop.backend.dto.OrderRequestItem;
import com.sweetshop.backend.model.OrderItem;
import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(new ArrayList<>()); // Initialize list

        double totalAmount = 0.0;

        for (OrderRequestItem itemRequest : request.getItems()) {
            Sweet sweet = sweetRepository.findById(itemRequest.getSweetId())
                    .orElseThrow(() -> new RuntimeException("Sweet not found"));

            // Check Stock
            if (sweet.getQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for sweet: " + sweet.getName());
            }

            // Reduce Stock
            sweet.setQuantity(sweet.getQuantity() - itemRequest.getQuantity());
            sweetRepository.save(sweet);

            // Create OrderItem
            OrderItem orderItem = new OrderItem(sweet, itemRequest.getQuantity(), sweet.getPrice());
            order.addItem(orderItem);

            totalAmount += sweet.getPrice() * itemRequest.getQuantity();
        }

        order.setTotalPrice(totalAmount);
        return orderRepository.save(order);
    }
}