package com.example.shoppingapi.modelhelper;

import java.math.BigDecimal;
import com.example.shoppingapi.model.Product;

public class ProductModelHelper implements ModelHelper<Product> {

    private final Long productId1 = 1L;
    private final String productname1 = "Cat Food";
    private final BigDecimal productemail1 = BigDecimal.valueOf(40000);

    private final Long productId2 = 2L;
    private final String productname2 = "Fish food";
    private final BigDecimal productemail2 = BigDecimal.valueOf(5000);
    
    public ProductModelHelper() {
    }
    

    @Override
    public Product createModel(Integer num){
        if (num == 1) {
            Product product1 = new Product();
            product1.setProductId(productId1);
            product1.setProductName(productname1);
            product1.setPrice(productemail1);
            return product1;
        } else {
            Product product2 = new Product();
            product2.setProductId(productId2);
            product2.setProductName(productname2);
            product2.setPrice(productemail2);
            return product2;
        }
    }

}