package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_variants_id", referencedColumnName = "id", nullable = false)
    private ProductVariant productVariant;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "line_total", precision = 10, scale = 2, nullable = false, insertable = false, updatable = false)
    private BigDecimal lineTotal;

    @PrePersist
    public void prePersist() {
        // Ensure lineTotal is calculated when the order item is created
        if (lineTotal == null) {
            lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
