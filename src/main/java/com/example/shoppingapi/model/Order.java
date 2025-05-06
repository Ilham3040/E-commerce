package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "order_date", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private LocalDateTime orderDate;

    @Builder.Default
    @Column(name = "shipping_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal shippingPrice = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "service_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal servicePrice = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "status", length = 50, nullable = false)
    private String status = "pending";

    @PrePersist
    public void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }
}
