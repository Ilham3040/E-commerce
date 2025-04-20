package com.example.shoppingapi.dto.response;

public class ProductDetailDTO {
    private Long productDetailId;
    private Long productId;




    public ProductDetailDTO(Long productDetailId,Long productId) {
        
        this.productDetailId = productDetailId;
        this.productId = productId;
    }

    public Long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetaproductDetailId(Long productDetailId) {
        this.productDetailId = productDetailId;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
