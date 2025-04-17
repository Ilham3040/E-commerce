package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductDetailRepository;
import com.example.shoppingapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProductDetailServiceTests.class)
public class ProductDetailServiceTests {

    @Mock
    private ProductDetailRepository productDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDetailService productDetailService;

    
    private ModelHelper<ProductDetail> productDetailHelper = ModelHelperFactory.getModelHelper(ProductDetail.class);

    @Test
    public void testFindAll() {
        ProductDetail pd1 = productDetailHelper.createModel(1);
        ProductDetail pd2 = productDetailHelper.createModel(2);
        List<ProductDetail> mockList = Arrays.asList(pd1, pd2);

        when(productDetailRepository.findAll()).thenReturn(mockList);

        List<ProductDetail> result = productDetailService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productDetailRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Found() {
        ProductDetail pd = productDetailHelper.createModel(1);
        when(productDetailRepository.findById(pd.getProductDetailId())).thenReturn(Optional.of(pd));

        Optional<ProductDetail> result = productDetailService.findById(pd.getProductDetailId());
        assertTrue(result.isPresent());
        assertEquals(pd.getProductDetailId(), result.get().getProductDetailId());
        verify(productDetailRepository, times(1)).findById(pd.getProductDetailId());
    }

    @Test
    public void testFindById_NotFound() {
        when(productDetailRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<ProductDetail> result = productDetailService.findById(1L);
        assertFalse(result.isPresent());
        verify(productDetailRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByProductId() {
        ProductDetail pd = productDetailHelper.createModel(1);
        Long productId = pd.getProduct().getProductId();
        when(productDetailRepository.findProductDetailbyProductId(productId)).thenReturn(Optional.of(pd));

        Optional<ProductDetail> result = productDetailService.findByProductId(productId);
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getProduct().getProductId());
        verify(productDetailRepository, times(1)).findProductDetailbyProductId(productId);
    }

    @Test
    public void testSaveProductDetail_Success() {
        ProductDetail pd = productDetailHelper.createModel(1);
        Product product = pd.getProduct();

        
        when(productRepository.existsById(product.getProductId())).thenReturn(true);
        when(productDetailRepository.save(any(ProductDetail.class))).thenReturn(pd);

        ProductDetail created = productDetailService.saveProductDetail(pd);
        assertNotNull(created);
        assertEquals(pd.getProductDetailId(), created.getProductDetailId());
        verify(productRepository, times(1)).existsById(product.getProductId());
        verify(productDetailRepository, times(1)).save(pd);
    }

    @Test
    public void testSaveProductDetail_ProductNotFound_ThrowsException() {
        ProductDetail pd = productDetailHelper.createModel(1);
        Product product = pd.getProduct();

        when(productRepository.existsById(product.getProductId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productDetailService.saveProductDetail(pd));
        assertEquals("Product not found. Cannot create product detail.", exception.getMessage());
        
        verify(productRepository, times(1)).existsById(product.getProductId());
        verify(productDetailRepository, never()).save(any(ProductDetail.class));
    }
    @Test
    public void testUpdateProductDetail() {
        ProductDetail existing = productDetailHelper.createModel(1);

        when(productDetailRepository.findById(existing.getProductDetailId())).thenReturn(Optional.of(existing));
        when(productDetailRepository.save(any(ProductDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDetail updated = existing.toBuilder().totalSold(300).build();
        ProductDetail result = productDetailService.updateProductDetail(existing.getProductDetailId(),updated);
    
        assertNotNull(result);
        assertEquals(updated, result);
    
        verify(productDetailRepository, times(2)).findById(existing.getProductDetailId());
        verify(productDetailRepository, times(1)).save(updated);
    }
    
    @Test
    public void testPartialUpdateProductDetail() {
        ProductDetail existing = productDetailHelper.createModel(1);
    
        when(productDetailRepository.findById(existing.getProductDetailId())).thenReturn(Optional.of(existing));
        when(productDetailRepository.save(any(ProductDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        Map<String, Object> updates = Map.of(
            "reviewRating", 4.75,
            "totalSold",    150
        );
        ProductDetail result = productDetailService.partialUpdateProductDetail(existing.getProductDetailId(),updates
        );
    
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(4.75).setScale(2),result.getReviewRating().setScale(2));
        assertEquals(150, result.getTotalSold());
    
        verify(productDetailRepository, times(1)).findById(existing.getProductDetailId());
        verify(productDetailRepository, times(1)).save(existing);
    }
    

    @Test
    public void testSoftdeleteById() {
        ProductDetail productDetail = productDetailHelper.createModel(1);

        when(productDetailRepository.findById(productDetail.getProductDetailId())).thenReturn(Optional.of(productDetail));
        when(productDetailRepository.save(any(ProductDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDetail deletedDetail = productDetailService.deleteById(productDetail.getProductDetailId());

        assertNotNull(deletedDetail);
        assertNotNull(deletedDetail.getDeletedAt());
        assertEquals(deletedDetail.getProductDetailId(), productDetail.getProductDetailId());

        verify(productDetailRepository, times(1)).save(productDetail);
        verify(productDetailRepository, times(1)).findById(productDetail.getProductDetailId());
    }

    @Test
    public void testSoftdeleteById_NotFound() {
        when(productDetailRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productDetailService.deleteById(1L);
            fail("Expected ResourceNotFoundException to be thrown");
        } catch (ResourceNotFoundException e) {
            assertEquals("Product Detail not found with ID: 1", e.getMessage());
        }

        verify(productDetailRepository, times(1)).findById(1L);
    }

}
