package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {

    @Mock private ProductDetailRepository detailRepo;
    @Mock private ProductRepository       productRepo;
    @InjectMocks private ProductDetailService service;

    private final ModelHelper<ProductDetail> helper =
        ModelHelperFactory.getModelHelper(ProductDetail.class);

    @Test
    void findAll_returnsAllDetails() {
        List<ProductDetail> list = List.of(helper.createModel(1), helper.createModel(2));
        when(detailRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(detailRepo).findAll();
    }

    @Test
    void findById_found_returnsDetail() {
        ProductDetail pd = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(pd));

        assertEquals(1L, service.findById(1L).getProductDetailId());
        verify(detailRepo).findById(1L);
    }

    @Test
    void findById_notFound_throwsException() {
        when(detailRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("ProductDetail not found with ID: 2", ex.getMessage());
    }

    @Test
    void findByProductId_found_returnsDetail() {
        ProductDetail pd = helper.createModel(1);
        Long pid = pd.getProduct().getProductId();
        when(detailRepo.findProductDetailbyProductId(pid)).thenReturn(Optional.of(pd));

        assertEquals(pid, service.findByProductId(pid).getProduct().getProductId());
        verify(detailRepo).findProductDetailbyProductId(pid);
    }

    @Test
    void findByProductId_notFound_throwsException() {
        when(detailRepo.findProductDetailbyProductId(3L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findByProductId(3L)
        );
        assertEquals("ProductDetail not found for product ID: 3", ex.getMessage());
    }

    @Test
    void saveProductDetail_missingProduct_throwsException() {
        ProductDetail pd = helper.createModel(1);
        pd.setProduct(null);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductDetail(pd)
        );
        assertEquals("Product ID is required to create a product detail.", ex.getMessage());
        verify(detailRepo, never()).save(any());
    }

    @Test
    void saveProductDetail_productNotFound_throwsException() {
        ProductDetail pd = helper.createModel(1);
        when(productRepo.existsById(pd.getProduct().getProductId())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.saveProductDetail(pd)
        );
        assertEquals("Product not found. Cannot create product detail.", ex.getMessage());
        verify(detailRepo, never()).save(any());
    }

    @Test
    void saveProductDetail_success_savesAndReturns() {
        ProductDetail pd = helper.createModel(1);
        when(productRepo.existsById(pd.getProduct().getProductId())).thenReturn(true);
        when(detailRepo.save(any())).thenReturn(pd);

        assertEquals(pd, service.saveProductDetail(pd));
        verify(detailRepo).save(pd);
    }

    @Test
    void updateProductDetail_idMismatch_throwsException() {
        ProductDetail pd = helper.createModel(1);
        pd.setProductDetailId(2L);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.updateProductDetail(1L, pd)
        );
        assertEquals("ProductDetail ID in URL and body must match.", ex.getMessage());
    }

    @Test
    void updateProductDetail_notFound_throwsException() {
        ProductDetail pd = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateProductDetail(1L, pd)
        );
        assertEquals("ProductDetail not found with ID: 1", ex.getMessage());
    }

    @Test
    void updateProductDetail_success_savesUpdated() {
        ProductDetail pd = helper.createModel(1);
        ProductDetail updated = pd.toBuilder().totalSold(500).build();
        when(detailRepo.findById(1L)).thenReturn(Optional.of(pd));
        when(detailRepo.save(any())).thenReturn(updated);

        assertEquals(updated, service.updateProductDetail(1L, updated));
        verify(detailRepo).save(updated);
    }

    @Test
    void partialUpdateProductDetail_existing_appliesUpdates() {
        ProductDetail pd = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(pd));
        when(detailRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String,Object> changes = Map.of(
            "reviewRating", 4.75,
            "totalSold",    150
        );
        ProductDetail result = service.partialUpdateProductDetail(1L, changes);

        assertEquals(BigDecimal.valueOf(4.75), result.getReviewRating());
        assertEquals(150, result.getTotalSold());
        verify(detailRepo).save(pd);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        ProductDetail original = helper.createModel(1);

        when(detailRepo.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(detailRepo).delete(original);

        service.deleteById(1L);

        verify(detailRepo).findById(1L);
        verify(detailRepo).delete(original);
    }

    @Test
    void softDeleteProductDetail_notFound_throwsException() {
        when(detailRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteById(2L)
        );
        assertEquals("ProductDetail not found with ID: 2", ex.getMessage());
    }
}
