package com.example.shoppingapi.dto;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long orderId;
    private String status;
    private LocalDateTime orderDate;

    private Long userId;
    private Long productId;

    public OrderDTO(Long orderId, String status, LocalDateTime orderDate,
                    Long userId, Long productId) {
        this.orderId = orderId;
        this.status = status;
        this.orderDate = orderDate;
        this.userId = userId;
        this.productId = productId;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
