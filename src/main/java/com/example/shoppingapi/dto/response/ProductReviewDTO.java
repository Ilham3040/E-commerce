package com.example.shoppingapi.dto.response;

public class ProductReviewDTO {
    private Long productReviewId;
    private Long userId;
    private Long productId;

    public ProductReviewDTO(Long productReviewId,Long userId,Long productId) {
        
        this.productReviewId = productReviewId;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getProductDetailId() {
        return productReviewId;
    }

    public void setProductDetaproductDetailId(Long productReviewId) {
        this.productReviewId = productReviewId;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long UserId){
        this.userId = UserId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
