package com.sweetshop.backend.repository;

import com.sweetshop.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sweetshop.backend.model.User;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}