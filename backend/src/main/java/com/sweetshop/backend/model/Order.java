package com.sweetshop.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "orders") // "order" is a reserved SQL keyword, so we use "orders"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    private Double totalPrice;

    // MISSING CASCADE HERE INTENTIONALLY to cause RED phase
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // Ensure cascade is ALL
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(User user, Double totalPrice) {
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
    }

    // Helper to add items
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}