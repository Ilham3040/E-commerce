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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductRepository;
import com.example.shoppingapi.repository.StoreRepository;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ProductServiceTests.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ProductService productService;

    private ModelHelper<Product> productHelper = ModelHelperFactory.getModelHelper(Product.class);
    private ModelHelper<Store> storeHelper =  ModelHelperFactory.getModelHelper(Store.class);

    @Test
    public void testGetAllProducts() {
        Product product1 = productHelper.createModel(1);
        Product product2 = productHelper.createModel(2);

        List<Product> mockProducts = Arrays.asList(product1,product2);

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1.getProductId(), result.get(0).getProductId());
        assertEquals(product1.getProductName(), result.get(0).getProductName());
        assertEquals(product1.getPrice(), result.get(0).getPrice());

        assertEquals(product2.getProductId(), result.get(1).getProductId());
        assertEquals(product2.getProductName(), result.get(1).getProductName());
        assertEquals(product2.getPrice(), result.get(1).getPrice());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductsById() {
        Product product = productHelper.createModel(1);

        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));

        Optional <Product> result = productService.getProductById(product.getProductId());

        assertNotNull(result);
        assertEquals(product.getProductId(), result.get().getProductId());
        assertEquals(product.getProductName(), result.get().getProductName());
        assertEquals(product.getPrice(), result.get().getPrice());


        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    public void testGetProductById_NotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(1L);
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateProduct() {
        Product product = productHelper.createModel(1);
        Store store = storeHelper.createModel(1);
        product.setStore(store);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        
        Product createdProduct = productService.saveProduct(product);

        assertNotNull(createdProduct);
        assertEquals(product.getProductId(), createdProduct.getProductId());
        assertEquals(product.getProductName(), createdProduct.getProductName());
        assertEquals(product.getPrice(), createdProduct.getPrice());

        verify(productRepository,times(1)).save(product);
        verify(storeRepository,times(1)).findById(store.getStoreId());

    }

    @Test
    public void testUpdateProduct() {
        Product existing = productHelper.createModel(1);
        
        when(productRepository.findById(existing.getProductId())).thenReturn(Optional.of(existing));
        when(storeRepository.findById(existing.getStore().getStoreId())).thenReturn(Optional.of(existing.getStore()));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = existing.toBuilder().price(BigDecimal.valueOf(35000)).build();
        Product result = productService.updateProduct(existing.getProductId(), updated);

        assertNotNull(result);
        assertEquals(existing.getProductId(), result.getProductId());
        assertEquals(BigDecimal.valueOf(35000), result.getPrice());

        verify(productRepository, times(1)).findById(existing.getProductId());
        verify(storeRepository, times(1)).findById(existing.getStore().getStoreId());
        verify(productRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateProduct() {
        Product existing = productHelper.createModel(1);

        when(productRepository.findById(existing.getProductId())).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of("price", 35000);
        Product result = productService.partialUpdateProduct(existing.getProductId(), updates);

        assertNotNull(result);
        assertEquals(existing.getProductId(), result.getProductId());
        assertEquals(BigDecimal.valueOf(35000), result.getPrice());

        verify(productRepository, times(1)).findById(existing.getProductId());
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    public void testSoftdeleteById() {
        Product product = productHelper.createModel(1);

        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product deletedProduct = productService.deleteById(product.getProductId());

        assertNotNull(deletedProduct);
        assertNotNull(deletedProduct.getDeletedAt());
        assertEquals(deletedProduct.getProductId(), product.getProductId());

        verify(productRepository, times(1)).save(product);
        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    public void testSoftdeleteById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            productService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Product not found with ID: 1", e.getMessage());
        }

        verify(productRepository, times(1)).findById(1L);
    }

}