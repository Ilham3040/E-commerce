package com.example.shoppingapi;

import com.example.shoppingapi.dto.create.*;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EntityCreationHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private StoreDetailService storeDetailService;

    public User createUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testuser");
        userCreateDTO.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        userCreateDTO.setPhoneNumber("1234567890");
        return userService.createUser(userCreateDTO);
    }

    public Store createStore(User user) throws Exception {
        StoreCreateDTO storeCreateDTO = new StoreCreateDTO();
        storeCreateDTO.setStoreName("Test Store");
        storeCreateDTO.setUserId(user.getUserId());
        return storeService.saveStore(storeCreateDTO);
    }

    public Product createProduct(Store store) throws Exception {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Product");
        productCreateDTO.setStoreId(store.getStoreId());
        return productService.saveProduct(productCreateDTO);
    }

    public Order createOrder(User user, Product product) throws Exception {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setUserId(user.getUserId());
        orderCreateDTO.setProductId(product.getProductId());
        orderCreateDTO.setStatus("pending");
        return orderService.saveOrder(orderCreateDTO);
    }

    public ProductDetail createProductDetail(Product product) throws Exception {
        ProductDetailCreateDTO productDetailCreateDTO = new ProductDetailCreateDTO();
        productDetailCreateDTO.setProductId(product.getProductId());
        productDetailCreateDTO.setDescription("Product description");
        return productDetailService.saveProductDetail(productDetailCreateDTO);
    }

    public StoreDetail createStoreDetail(Store store) throws Exception {
        StoreDetailCreateDTO storeDetailCreateDTO = new StoreDetailCreateDTO();
        storeDetailCreateDTO.setStoreId(store.getStoreId());
        storeDetailCreateDTO.setAddress("Test Address");
        storeDetailCreateDTO.setDescription("Test Description");
        return storeDetailService.saveStoreDetail(storeDetailCreateDTO);
    }
}
