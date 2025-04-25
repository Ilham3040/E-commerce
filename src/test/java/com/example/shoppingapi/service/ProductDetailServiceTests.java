package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductDetailCreateDTO;
import com.example.shoppingapi.dto.patch.ProductDetailPatchDTO;
import com.example.shoppingapi.dto.put.ProductDetailPutDTO;
import com.example.shoppingapi.model.Product;
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
        ProductDetail productDetail = helper.createModel(1);
        when(detailRepo.findById(1L)).thenReturn(Optional.of(productDetail));

        assertEquals(1L, service.findById(1L).getProductDetailId());
        verify(detailRepo).findById(1L);
    }

    @Test
    void findByProductId_found_returnsDetail() {
        ProductDetail productDetail = helper.createModel(1);
        Long id = productDetail.getProduct().getProductId();
        when(detailRepo.findProductDetailbyProductId(id)).thenReturn(Optional.of(productDetail));

        assertEquals(id, service.getProductDetailByProductId(id).getProduct().getProductId());
        verify(detailRepo).findProductDetailbyProductId(id);
    }

    @Test
    void findByProductId_notFound_throwsException() {
        when(detailRepo.findProductDetailbyProductId(3L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getProductDetailByProductId(3L)
        );
        assertEquals("ProductDetail not found for product ID: 3", ex.getMessage());
    }


    @Test
    void saveProductDetail_productNotFound_throwsException() {
        ProductDetail productDetail = helper.createModel(1);
        when(productRepo.findById(productDetail.getProduct().getProductId())).thenReturn(Optional.empty());

        ProductDetailCreateDTO productDetailCreateDTO = new ProductDetailCreateDTO();
        productDetailCreateDTO.setProductId(productDetail.getProductDetailId());
        productDetailCreateDTO.setDescription(productDetail.getDescription());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.saveProductDetail(productDetailCreateDTO)
        );
        assertEquals("Product not found. Cannot create product detail.", ex.getMessage());
        verify(detailRepo, never()).save(any());
    }

    @Test
    void saveProductDetail_success_savesAndReturns() {
        ProductDetail productDetail = helper.createModel(1);
        when(productRepo.findById(productDetail.getProduct().getProductId())).thenReturn(Optional.of(productDetail.getProduct()));

        ProductDetailCreateDTO productDetailCreateDTO = new ProductDetailCreateDTO();
        productDetailCreateDTO.setProductId(productDetail.getProductDetailId());
        productDetailCreateDTO.setDescription(productDetail.getDescription());

        ProductDetail toSave = ProductDetail.builder()
                .product(Product.builder().productId(productDetailCreateDTO.getProductId()).build())
                .description(productDetailCreateDTO.getDescription())
                .build();

        when(detailRepo.save(any())).thenReturn(productDetail);

        ProductDetail result = service.saveProductDetail(productDetailCreateDTO);
        assertEquals(result.getProductDetailId(),productDetail.getProductDetailId() );
        verify(detailRepo).save(toSave);
    }


    @Test
    void updateProductDetail_notFound_throwsException() {
        ProductDetail productDetail = helper.createModel(1);
        when(detailRepo.findProductDetailbyProductId(1L)).thenReturn(Optional.empty());

        ProductDetailPutDTO productDetailPutDTO = new ProductDetailPutDTO();
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateProductDetail(1L, productDetailPutDTO)
        );
        assertEquals("ProductDetail not found for product ID: 1", ex.getMessage());
    }

    @Test
    void updateProductDetail_success_savesUpdated() {
        ProductDetail productDetail = helper.createModel(1);

        ProductDetailPutDTO productDetailPutDTO = new ProductDetailPutDTO();
        productDetailPutDTO.setDescription("Random bullshit go");

        when(detailRepo.findProductDetailbyProductId(1L)).thenReturn(Optional.of(productDetail));
        when(detailRepo.save(any())).thenReturn(productDetail);

        ProductDetail result  = service.updateProductDetail(1L, productDetailPutDTO);

        assertEquals(productDetailPutDTO.getDescription(), result.getDescription());
        verify(detailRepo).save(productDetail);
        verify(detailRepo).findProductDetailbyProductId(1L);
    }

    @Test
    void partialUpdateProductDetail_existing_appliesUpdates() {
        ProductDetail productDetail = helper.createModel(1);

        ProductDetailPatchDTO productDetailPatchDTO = new ProductDetailPatchDTO();
        productDetailPatchDTO.setDescription("Random bullshit go");

        when(detailRepo.findProductDetailbyProductId(1L)).thenReturn(Optional.of(productDetail));
        when(detailRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductDetail result = service.partialUpdateProductDetail(1L, productDetailPatchDTO);

        assertEquals(productDetail.getDescription(),result.getDescription());
        verify(detailRepo).findProductDetailbyProductId(1L);
        verify(detailRepo).save(productDetail);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        ProductDetail original = helper.createModel(1);

        when(detailRepo.findProductDetailbyProductId(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(detailRepo).delete(original);

        service.deleteByProductId(1L);

        verify(detailRepo).findProductDetailbyProductId(1L);
        verify(detailRepo).delete(original);
    }

    @Test
    void softDeleteProductDetail_notFound_throwsException() {
        when(detailRepo.findProductDetailbyProductId(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteByProductId(2L)
        );
        assertEquals("ProductDetail not found for product ID: 2", ex.getMessage());
    }
}
