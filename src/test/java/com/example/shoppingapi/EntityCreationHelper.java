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

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentVendorService shipmentVendorService;

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

    public ProductVariant createProductVariant(Product product) throws Exception {
        ProductVariantCreateDTO productVariantCreateDTO = new ProductVariantCreateDTO();
        productVariantCreateDTO.setVariantName("Test Variant");
        productVariantCreateDTO.setProductId(product.getProductId());
        productVariantCreateDTO.setPrice(new BigDecimal("10.99"));
        productVariantCreateDTO.setStockQuantity(100);
        return productVariantService.saveProductVariant(productVariantCreateDTO);
    }

    public ProductReview createProductReview(User user, Product product) throws Exception {
        ProductReviewCreateDTO productReviewCreateDTO = new ProductReviewCreateDTO();
        productReviewCreateDTO.setProductId(product.getProductId());
        productReviewCreateDTO.setUserId(user.getUserId());
        productReviewCreateDTO.setStarRating(5);
        productReviewCreateDTO.setDescription("Excellent product!");
        return productReviewService.saveProductReview(productReviewCreateDTO);
    }

    public Shipment createShipment(Order order, Long vendorId) throws Exception {
        ShipmentCreateDTO shipmentCreateDTO = new ShipmentCreateDTO();
        shipmentCreateDTO.setOrderId(order.getOrderId());
        shipmentCreateDTO.setVendorId(vendorId);
        return shipmentService.saveShipment(shipmentCreateDTO);
    }

    public ShipmentVendor createShipmentVendor() throws Exception {
        ShipmentVendorCreateDTO shipmentVendorCreateDTO = new ShipmentVendorCreateDTO();
        shipmentVendorCreateDTO.setVendorName("Test Vendor");
        shipmentVendorCreateDTO.setVendorContact("1234567890");
        shipmentVendorCreateDTO.setVendorEmail("testvendor@example.com");
        shipmentVendorCreateDTO.setOfficialWebsiteUrl("http://testvendor.com");
        return shipmentVendorService.saveShipmentVendor(shipmentVendorCreateDTO);
    }
}
