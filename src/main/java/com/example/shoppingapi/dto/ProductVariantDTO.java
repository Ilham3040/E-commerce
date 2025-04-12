package com.example.shoppingapi.dto;

public class ProductVariantDTO {
    private Long productVariantId;
    private Long productId;




    public ProductVariantDTO(Long productVariantId,Long productId) {
        
        this.productVariantId = productVariantId;
        this.productId = productId;
    }

    public Long getProductDetailId() {
        return productVariantId;
    }

    public void setProductDetaproductDetailId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
