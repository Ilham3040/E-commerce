package com.example.shoppingapi.dto.response;

public class UserCartDTO {
    private Long cartId;
    private Long userId;
    private Long productId;

    public UserCartDTO(Long cartId, Long userId, Long productId) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
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
