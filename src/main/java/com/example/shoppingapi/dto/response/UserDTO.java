package com.example.shoppingapi.dto.response;

public class UserDTO {
    private Long userId;

    public UserDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
