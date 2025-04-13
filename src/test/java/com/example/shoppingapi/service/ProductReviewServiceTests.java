package com.example.shoppingapi.service;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProductReviewServiceTests.class)
public class ProductReviewServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductReviewRepository productReviewRepository;

    @InjectMocks
    private ProductReviewService productReviewService;

    private ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);
    private ModelHelper<ProductReview> productReviewHelper = ModelHelperFactory.getModelHelper(ProductReview.class);
    private ModelHelper<User> userHelper =  ModelHelperFactory.getModelHelper(User.class);


    @Test
    public void testSavePtoductReview() {
        ProductReview productReview = productReviewHelper.createModel(1);
        User user = userHelper.createModel(1);
        productReview.setUser(user);
        Product product = productHelper.createModel(1);
        productReview.setProduct(product);


        when(productReviewRepository.save(any(ProductReview.class))).thenReturn(productReview);
        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        when(productRepository.existsById(product.getProductId())).thenReturn(true);

        ProductReview savProductReview = productReviewService.saveProductReview(productReview);
        

        assertNotNull(savProductReview);
        assertEquals(productReview.getReviewId(), savProductReview.getReviewId());
        assertEquals(productReview.getLikes(), savProductReview.getLikes());
        assertEquals(productReview.getStarRating(), savProductReview.getStarRating());
        assertEquals(productReview.getDescription(),savProductReview.getDescription());

        verify(productReviewRepository,times(1)).save(productReview);
        verify(productRepository,times(1)).existsById(product.getProductId());
        verify(userRepository,times(1)).existsById(user.getUserId());

    }
    
}
