package com.example.shoppingapi.dto.response;

public class ProductReviewDTO {
    private Long productReviewId;
    private Long productId;




    public ProductReviewDTO(Long productReviewId,Long productId) {
        
        this.productReviewId = productReviewId;
        this.productId = productId;
    }

    public Long getProductDetailId() {
        return productReviewId;
    }

    public void setProductDetaproductDetailId(Long productReviewId) {
        this.productReviewId = productReviewId;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
