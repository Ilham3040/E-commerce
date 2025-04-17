package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.repository.StoreRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StoreCategoryServiceTests.class)
public class StoreCategoryServiceTests {

    @Mock
    private StoreCategoryRepository storeCategoryRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreCategoryService storeCategoryService;

    private ModelHelper<StoreCategory> storeCategoryHelper = ModelHelperFactory.getModelHelper(StoreCategory.class);

    @Test
    public void testFindAll() {
        StoreCategory storeCategory1 = storeCategoryHelper.createModel(1);
        StoreCategory storeCategory2 = storeCategoryHelper.createModel(2);

        when(storeCategoryRepository.findAll()).thenReturn(Arrays.asList(storeCategory1, storeCategory2));

        var result = storeCategoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Arrays.asList(storeCategory1, storeCategory2), result);

        verify(storeCategoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        StoreCategory storeCategory = storeCategoryHelper.createModel(1);
        when(storeCategoryRepository.findById(storeCategory.getCategoryId())).thenReturn(Optional.of(storeCategory));

        StoreCategory result = storeCategoryService.findById(storeCategory.getCategoryId()).orElseThrow(() -> new AssertionError("StoreCategory not found"));

        assertNotNull(result);
        assertEquals(storeCategory, result);

        verify(storeCategoryRepository, times(1)).findById(storeCategory.getCategoryId());
    }

    @Test
    public void testFindById_NotFound() {
        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StoreCategory> result = storeCategoryService.findById(1L);

        assertFalse(result.isPresent());

        verify(storeCategoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveStoreCategory() {
        StoreCategory storeCategory = storeCategoryHelper.createModel(1);

        when(storeCategoryRepository.save(any(StoreCategory.class))).thenReturn(storeCategory);
        when(storeRepository.findById(storeCategory.getStore().getStoreId())).thenReturn(Optional.of(storeCategory.getStore()));

        StoreCategory createdStoreCategory = storeCategoryService.saveStoreCategory(storeCategory);

        assertNotNull(createdStoreCategory);
        assertEquals(storeCategory, createdStoreCategory);

        verify(storeCategoryRepository, times(1)).save(storeCategory);
        verify(storeRepository,times(1)).findById(storeCategory.getStore().getStoreId());
    }

    @Test
    public void testUpdateStoreCategory() {
        StoreCategory existing = storeCategoryHelper.createModel(1);
        
        when(storeCategoryRepository.findById(existing.getCategoryId())).thenReturn(Optional.of(existing));
        when(storeCategoryRepository.save(any(StoreCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeRepository.findById(existing.getStore().getStoreId())).thenReturn(Optional.of(existing.getStore()));

        StoreCategory updated = existing.toBuilder().categoryName("Ubur-Ubur Ikan Lele").build();
        StoreCategory result = storeCategoryService.updateStoreCategory(existing.getCategoryId(), updated);

        assertNotNull(result);
        assertEquals(updated, result);

        verify(storeCategoryRepository, times(1)).findById(existing.getCategoryId());
        verify(storeRepository,times(1)).findById(existing.getStore().getStoreId());
        verify(storeCategoryRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateStoreCategory() {
        StoreCategory existing = storeCategoryHelper.createModel(1);

        when(storeCategoryRepository.findById(existing.getCategoryId())).thenReturn(Optional.of(existing));
        when(storeCategoryRepository.save(any(StoreCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of(
            "categoryName", "Ubur-Ubur Ikan Lele"
        );
        StoreCategory result = storeCategoryService.partialUpdateStoreCategory(existing.getCategoryId(), updates);

        assertNotNull(result);
        assertEquals("Ubur-Ubur Ikan Lele", result.getCategoryName());

        verify(storeCategoryRepository, times(1)).findById(existing.getCategoryId());
        verify(storeCategoryRepository, times(1)).save(existing);
    }


    @Test
    public void testDeleteStoreCategory() {
        StoreCategory storeCategory = storeCategoryHelper.createModel(1);

        when(storeCategoryRepository.findById(storeCategory.getCategoryId())).thenReturn(Optional.of(storeCategory));

        storeCategoryService.deleteById(storeCategory.getCategoryId());

        verify(storeCategoryRepository, times(1)).findById(storeCategory.getCategoryId());
    }

    @Test
    public void testDeleteStoreCategory_NotFound() {
        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            storeCategoryService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("StoreCategory not found with ID: 1", e.getMessage());
        }

        verify(storeCategoryRepository, times(1)).findById(1L);
    }
}
