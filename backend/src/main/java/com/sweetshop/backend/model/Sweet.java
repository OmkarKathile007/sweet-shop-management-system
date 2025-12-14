//package com.sweetshop.backend.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Min; // Add import
//import jakarta.validation.constraints.NotNull;
//
//@Entity
//@Table(name = "sweets")
//public class Sweet {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    private String category;
//
//
//    @NotNull(message = "Price is required")
//    @Min(value = 0, message = "Price cannot be negative")
//    private Double price; // We will add validation later to fix the test
//
//    private Integer quantity;
//    @Column(length = 500) // Allow longer text for descriptions
//    private String description;
//
//    public Sweet() {}
//
//    public Sweet(String name, String category, Double price, Integer quantity, String description) {
//        this.name = name;
//        this.category = category;
//        this.price = price;
//        this.quantity = quantity;
//        this.description = description;
//    }
//
//    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getCategory() { return category; }
//    public void setCategory(String category) { this.category = category; }
//
//    public Double getPrice() { return price; }
//    public void setPrice(Double price) { this.price = price; }
//
//    public Integer getQuantity() { return quantity; }
//    public void setQuantity(Integer quantity) { this.quantity = quantity; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//}


package com.sweetshop.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "sweets")
public class Sweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    private String description;

    // --- 1. Default Constructor (Required by JPA/Hibernate) ---
    public Sweet() {
    }

    // --- 2. Parameterized Constructor (Required by your Tests) ---
    // This matches: new Sweet("Name", "Category", 10.0, 5)
    public Sweet(String name, String category, Double price, Integer quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
    public Sweet(String name, String category, Double price, Integer quantity, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // --- 3. Getters and Setters (Since no Lombok) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}