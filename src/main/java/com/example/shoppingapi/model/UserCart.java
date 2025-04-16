package com.example.shoppingapi.model;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "user_cart")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
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
