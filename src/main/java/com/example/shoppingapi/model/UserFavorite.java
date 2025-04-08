package com.example.shoppingapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_favorite")
@Data
public class UserFavorite {
    @EmbeddedId
    private UserFavoriteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
