package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserCart {
    
    @EmbeddedId
    private UserCartId id;
    
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id",nullable = false)
    private User user;
    
    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName="id",nullable = false)
    private Product product;
    
    @Builder.Default
    @Column(name = "quantity", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer quantity = 1;
}
