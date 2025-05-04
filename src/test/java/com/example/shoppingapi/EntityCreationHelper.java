package com.example.shoppingapi;

import com.example.shoppingapi.dto.create.*;
import com.example.shoppingapi.model.*;
import com.example.shoppingapi.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

@Transactional
@Rollback(true)
@Component
public class EntityCreationHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCartService userCartService;

    @Autowired
    private UserFavoriteService userFavoriteService;

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

    @Autowired
    private StoreCategoryService storeCategoryService;

    @Autowired
    private StoreRoleService storeRoleService;

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

    public StoreCategory createStoreCategory(Store store) throws Exception {
        StoreCategoryCreateDTO storeCategoryCreateDTO = new StoreCategoryCreateDTO();
        storeCategoryCreateDTO.setStoreId(store.getStoreId());
        storeCategoryCreateDTO.setCategoryName("new category"); // Assign the category name
        return storeCategoryService.saveStoreCategory(storeCategoryCreateDTO);
    }

    public StoreRole createStoreRole(Store store, User user) throws Exception {
        StoreRoleCreateDTO storeRoleCreateDTO = new StoreRoleCreateDTO();
        storeRoleCreateDTO.setStoreId(store.getStoreId());
        storeRoleCreateDTO.setUserId(user.getUserId());
        return storeRoleService.promoteToAdminStoreRole(storeRoleCreateDTO);
    }

    public UserCart createUserCart(User user, Product product) throws Exception {
        UserCartCreateDTO userCartCreateDTO = new UserCartCreateDTO();
        userCartCreateDTO.setUserId(user.getUserId());
        userCartCreateDTO.setProductId(product.getProductId());
        return userCartService.addingUserCart(userCartCreateDTO);
    }

    public UserFavorite createUserFavorite(User user, Product product) throws Exception {
        UserFavoriteCreateDTO userFavoriteCreateDTO = new UserFavoriteCreateDTO();
        userFavoriteCreateDTO.setUserId(user.getUserId());
        userFavoriteCreateDTO.setProductId(product.getProductId());
        return userFavoriteService.addingUserFavorite(userFavoriteCreateDTO);
    }
}
