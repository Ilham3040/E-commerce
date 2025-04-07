package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class UserFavoriteId implements Serializable {
    private Long userId;
    private Long productId;
}
