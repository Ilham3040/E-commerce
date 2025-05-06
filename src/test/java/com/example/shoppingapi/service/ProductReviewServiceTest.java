package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductReviewCreateDTO;
import com.example.shoppingapi.dto.patch.ProductReviewPatchDTO;
import com.example.shoppingapi.dto.put.ProductReviewPutDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductReview;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductReviewRepository;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductReviewServiceTest {

    @Mock private ProductReviewRepository reviewRepo;
    @Mock private ProductRepository       productRepo;
    @Mock private UserRepository          userRepo;
    @InjectMocks private ProductReviewService service;

    private final ModelHelper<ProductReview> helper =
        ModelHelperFactory.getModelHelper(ProductReview.class);

    @Test
    void findAll_returnsAllReviews() {
        List<ProductReview> list = List.of(helper.createModel(1), helper.createModel(2));
        when(reviewRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(reviewRepo).findAll();
    }

    @Test
    void findById_found_returnsReview() {
        ProductReview productReview = helper.createModel(1);
        when(reviewRepo.findById(1L)).thenReturn(Optional.of(productReview));

        assertEquals(productReview, service.getProductReviewById(1L));
        verify(reviewRepo).findById(1L);
    }

    @Test
    void findById_notFound_throwsException() {
        when(reviewRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getProductReviewById(2L)
        );
        assertEquals("ProductReview not found with ID: 2", ex.getMessage());
    }

    @Test
    void saveProductReview_success_savesAndReturns() {
        ProductReview productReview = helper.createModel(1);
        when(productRepo.findById(productReview.getProduct().getProductId())).thenReturn(Optional.of(productReview.getProduct()));
        when(userRepo.findById(productReview.getUser().getUserId())).thenReturn(Optional.of(productReview.getUser()));


        ProductReviewCreateDTO productReviewCreateDTO = new ProductReviewCreateDTO();
        productReviewCreateDTO.setUserId(productReview.getUser().getUserId());
        productReviewCreateDTO.setProductId(productReview.getProduct().getProductId());
        productReviewCreateDTO.setStarRating(productReview.getStarRating());
        productReviewCreateDTO.setDescription(productReview.getDescription());

        ProductReview review = ProductReview.builder()
                .user(User.builder().userId(productReviewCreateDTO.getUserId()).build())
                .product(Product.builder().productId(productReviewCreateDTO.getProductId()).build())
                .starRating(productReviewCreateDTO.getStarRating())
                .description(productReviewCreateDTO.getDescription())
                .build();

        when(reviewRepo.save(any())).thenReturn(review);

        ProductReview result = service.saveProductReview(productReviewCreateDTO);
        assertEquals(review.getProduct().getProductId(), result.getProduct().getProductId());
        assertEquals(review.getUser().getUserId(), result.getUser().getUserId());
        verify(reviewRepo).save(review);
        verify(userRepo).findById(productReview.getUser().getUserId());
        verify(productRepo).findById(productReview.getProduct().getProductId());

    }


    @Test
    void saveProductReview_productNotFound_throwsException() {
        ProductReview productReview = helper.createModel(1);
        when(productRepo.findById(productReview.getProduct().getProductId())).thenReturn(Optional.empty());
        when(userRepo.findById(productReview.getUser().getUserId())).thenReturn(Optional.of(productReview.getUser()));

        ProductReviewCreateDTO productReviewCreateDTO = new ProductReviewCreateDTO();
        productReviewCreateDTO.setUserId(productReview.getUser().getUserId());
        productReviewCreateDTO.setProductId(productReview.getProduct().getProductId());
        productReviewCreateDTO.setStarRating(productReview.getStarRating());
        productReviewCreateDTO.setDescription(productReview.getDescription());

        ProductReview review = ProductReview.builder()
                .user(User.builder().userId(productReviewCreateDTO.getUserId()).build())
                .product(Product.builder().productId(productReviewCreateDTO.getProductId()).build())
                .starRating(productReviewCreateDTO.getStarRating())
                .description(productReviewCreateDTO.getDescription())
                .build();

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.saveProductReview(productReviewCreateDTO)
        );
        assertEquals("Product not found with ID: 1 cannot create product review",ex.getMessage());
    }

    @Test
    void updateProductReview_success_savesUpdated() {
        ProductReview productReview = helper.createModel(1);

        when(reviewRepo.findById(1L)).thenReturn(Optional.of(productReview));
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));


        ProductReviewPutDTO productReviewPutDTO = new ProductReviewPutDTO();
        productReviewPutDTO.setStarRating(3);
        productReviewPutDTO.setDescription(productReview.getDescription());

        ProductReview res = service.updateProductReview(1L, productReviewPutDTO);

        assertEquals(productReviewPutDTO.getStarRating(), res.getStarRating());
        verify(reviewRepo).save(productReview);
    }

    @Test
    void partialUpdateProductReview_appliesUpdates() {
        ProductReview productReview = helper.createModel(1);

        when(reviewRepo.findById(1L)).thenReturn(Optional.of(productReview));
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));


        ProductReviewPatchDTO productReviewPatchDTO = new ProductReviewPatchDTO();
        productReviewPatchDTO.setStarRating(3);

        ProductReview res = service.partiallyUpdateProductReview(1L, productReviewPatchDTO);

        assertEquals(productReviewPatchDTO.getStarRating(), res.getStarRating());
        verify(reviewRepo).save(productReview);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        ProductReview original = helper.createModel(1);

        when(reviewRepo.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(reviewRepo).delete(original);

        service.deleteById(1L);

        verify(reviewRepo).findById(1L);
        verify(reviewRepo).delete(original);
    }
}
