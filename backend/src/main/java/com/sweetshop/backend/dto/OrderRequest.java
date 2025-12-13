package com.sweetshop.backend.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderRequestItem> items;

    // Getters and Setters
    public List<OrderRequestItem> getItems() { return items; }
    public void setItems(List<OrderRequestItem> items) { this.items = items; }
}