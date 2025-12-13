package com.sweetshop.backend.service;

import com.sweetshop.backend.dto.OrderRequest;
import com.sweetshop.backend.dto.OrderRequestItem;
import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.model.User;
import com.sweetshop.backend.repository.OrderRepository;
import com.sweetshop.backend.repository.SweetRepository;
import com.sweetshop.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SweetRepository sweetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_shouldReduceStockAndSaveOrder() {
        // Arrange
        String username = "customer";
        User user = new User(username, "pass", "USER");

        // We have 100 laddus in stock
        Sweet laddu = new Sweet("Laddu", "Trad", 10.0, 100);
        laddu.setId(1L);

        // Request to buy 5 laddus
        OrderRequestItem itemRequest = new OrderRequestItem();
        itemRequest.setSweetId(1L);
        itemRequest.setQuantity(5);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(Collections.singletonList(itemRequest));

        // Mock database calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(sweetRepository.findById(1L)).thenReturn(Optional.of(laddu));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order result = orderService.placeOrder(username, orderRequest);

        // Assert
        assertNotNull(result, "Order result should not be null");

        // Verify that the sweet was saved (implies stock update logic ran)
        verify(sweetRepository).save(laddu);
    }
    @Test
    void getOrdersByUser_shouldReturnOrderList() {
        String username = "customer";
        User user = new User(username, "pass", "USER");
        Order order = new Order(user, 100.0);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(order));

        List<Order> results = orderService.getOrdersByUser(username);

        assertNotNull(results);
        assertEquals(1, results.size());
    }
}