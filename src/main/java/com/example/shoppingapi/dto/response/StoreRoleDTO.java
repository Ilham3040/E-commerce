package com.example.shoppingapi.dto.response;

public class StoreRoleDTO {
    private Long userId;
    private Long storeId;
    private String role;

    public StoreRoleDTO(Long userId, Long storeId, String role) {
        this.userId = userId;
        this.storeId = storeId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
