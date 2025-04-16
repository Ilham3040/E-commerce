package com.example.shoppingapi.model;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "user_favorite")
@Data
@Builder(toBuilder = true)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
public class UserFavorite {
    @EmbeddedId
    private UserFavoriteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",referencedColumnName="id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName="id",nullable = false)
    private Product product;
}
