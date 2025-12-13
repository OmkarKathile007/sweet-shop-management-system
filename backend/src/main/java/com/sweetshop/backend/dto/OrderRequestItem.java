package com.sweetshop.backend.dto;

public class OrderRequestItem {
    private Long sweetId;
    private Integer quantity;

    // Getters and Setters
    public Long getSweetId() { return sweetId; }
    public void setSweetId(Long sweetId) { this.sweetId = sweetId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}