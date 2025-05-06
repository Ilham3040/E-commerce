package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ProductVariantCreateDTO;
import com.example.shoppingapi.dto.patch.ProductVariantPatchDTO;
import com.example.shoppingapi.dto.put.ProductVariantPutDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.ProductVariant;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ProductVariantRepository;
import com.example.shoppingapi.repository.ProductRepository;
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
class ProductVariantServiceTest {

    @Mock private ProductVariantRepository variantRepo;
    @Mock private ProductRepository        productRepo;
    @InjectMocks private ProductVariantService service;

    private final ModelHelper<ProductVariant> helper =
        ModelHelperFactory.getModelHelper(ProductVariant.class);

    @Test
    void findAll_returnsAllVariants() {
        List<ProductVariant> list = List.of(helper.createModel(1), helper.createModel(2));
        when(variantRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(variantRepo).findAll();
    }

    @Test
    void findById_found_returnsVariant() {
        ProductVariant productVariant = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(productVariant));

        assertEquals(productVariant, service.getProductVariantById(1L));
    }

    @Test
    void findById_notFound_throwsException() {
        when(variantRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getProductVariantById(2L)
        );
        assertEquals("ProductVariant not found with ID: 2", ex.getMessage());
    }

    @Test
    void saveProductVariant_success_savesAndReturns() {
        ProductVariant productVariant = helper.createModel(1);
        when(productRepo.findById(productVariant.getProduct().getProductId())).thenReturn(Optional.of(productVariant.getProduct()));

        ProductVariantCreateDTO productVariantCreateDTO = new ProductVariantCreateDTO();
        productVariantCreateDTO.setProductId(productVariant.getProduct().getProductId());
        productVariantCreateDTO.setVariantName(productVariant.getVariantName());
        productVariantCreateDTO.setPrice(productVariant.getPrice());
        productVariantCreateDTO.setStockQuantity(productVariantCreateDTO.getStockQuantity());

        ProductVariant variant = ProductVariant.builder()
                .product(Product.builder().productId(productVariantCreateDTO.getProductId()).build())
                .price(productVariant.getPrice())
                .variantName(productVariant.getVariantName())
                .stockQuantity(productVariantCreateDTO.getStockQuantity())
                .build();

        when(variantRepo.save(any())).thenReturn(variant);

        ProductVariant result = service.saveProductVariant(productVariantCreateDTO);
        assertEquals(variant.getProduct().getProductId(), result.getProduct().getProductId());
        assertEquals(variant.getVariantName(), result.getVariantName());
        verify(variantRepo).save(variant);
        verify(productRepo).findById(productVariant.getProduct().getProductId());

    }

    @Test
    void saveProductVariant_productNotFound_throwsException() {
        ProductVariant productVariant = helper.createModel(1);

        when(productRepo.findById(productVariant.getProduct().getProductId())).thenReturn(Optional.empty());

        ProductVariantCreateDTO productVariantCreateDTO = new ProductVariantCreateDTO();
        productVariantCreateDTO.setProductId(productVariant.getProduct().getProductId());
        productVariantCreateDTO.setVariantName(productVariant.getVariantName());
        productVariantCreateDTO.setPrice(productVariant.getPrice());
        productVariantCreateDTO.setStockQuantity(productVariantCreateDTO.getStockQuantity());


        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.saveProductVariant(productVariantCreateDTO)
        );
        assertEquals("Product with ID : 1 not  found cannot create Product Variant" , ex.getMessage());
    }


    @Test
    void updateProductVariant_success_savesUpdated() {
        ProductVariant productVariant = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(productVariant));

        ProductVariantPutDTO productVariantPutDTO = new ProductVariantPutDTO();
        productVariantPutDTO.setVariantName(productVariant.getVariantName());
        productVariantPutDTO.setPrice(productVariant.getPrice());
        productVariantPutDTO.setStockQuantity(100);

        when(variantRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductVariant result = service.updateProductVariant(1L,productVariantPutDTO);
        assertEquals(productVariant.getProduct().getProductId(), result.getProduct().getProductId());
        assertEquals(productVariant.getVariantName(), result.getVariantName());
        verify(variantRepo).save(productVariant);
        verify(variantRepo).findById(1L);


    }

    @Test
    void partialUpdateProductVariant_appliesUpdates() {
        ProductVariant productVariant = helper.createModel(1);
        when(variantRepo.findById(1L)).thenReturn(Optional.of(productVariant));
        when(variantRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductVariantPatchDTO productVariantPatchDTO = new ProductVariantPatchDTO();
        productVariantPatchDTO.setStockQuantity(100);

        ProductVariant result = service.partiallyUpdateProductVariant(1L, productVariantPatchDTO);

        assertEquals(productVariant.getProduct().getProductId(), result.getProduct().getProductId());
        assertEquals(productVariant.getVariantName(), result.getVariantName());
        verify(variantRepo).save(productVariant);
        verify(variantRepo).findById(1L);
    }

    @Test
    void deleteById_existing_invokesSoftDelete() {

        ProductVariant original = helper.createModel(1);

        when(variantRepo.findById(1L))
            .thenReturn(Optional.of(original));
        doNothing().when(variantRepo).delete(original);

        service.deleteById(1L);

        verify(variantRepo).findById(1L);
        verify(variantRepo).delete(original);
    }
}
