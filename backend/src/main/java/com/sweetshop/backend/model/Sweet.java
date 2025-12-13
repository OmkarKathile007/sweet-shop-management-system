package com.sweetshop.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min; // Add import
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sweets")
public class Sweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;


    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price; // We will add validation later to fix the test

    private Integer quantity;

    public Sweet() {}

    public Sweet(String name, String category, Double price, Integer quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}