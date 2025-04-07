package com.example.shoppingapi.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class StoreCategoryItemId implements Serializable {
    private Long categoryId;
    private Long productId;


    public Long getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(Long categoryId){
        this.categoryId = categoryId;
    }

    public Long getProductId(){
        return productId;
    }

    public void setProductId(Long productId){
        this.productId = productId;
    }
}
