package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class UserFavoriteId implements Serializable {
    private Long userId;
    private Long productId;
}
