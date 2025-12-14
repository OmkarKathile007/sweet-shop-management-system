package com.sweetshop.backend.repository;

import com.sweetshop.backend.model.Order;
import com.sweetshop.backend.model.OrderItem;
import com.sweetshop.backend.model.Sweet;
import com.sweetshop.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager; // Helper to prep database data

    @Test
    void save_shouldPersistOrderAndItems() {
        // 1. Create dependencies
        User user = new User("customer", "pass", "USER");
        entityManager.persist(user);

        Sweet sweet = new Sweet("Kaju Katli", "Premium", 50.0, 100,"");
        entityManager.persist(sweet);

        // 2. Create Order
        Order order = new Order(user, 100.0);

        // 3. Create Item and link it
        OrderItem item = new OrderItem(sweet, 2, 50.0);
        order.addItem(item);

        // 4. Save Order (This should cascade and save the item too)
        Order savedOrder = orderRepository.saveAndFlush(order);

        // 5. Verify
        assertNotNull(savedOrder.getId());
        assertEquals(1, savedOrder.getItems().size());
    }
}