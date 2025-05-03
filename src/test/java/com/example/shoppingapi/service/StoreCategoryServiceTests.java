package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.StoreCategoryCreateDTO;
import com.example.shoppingapi.dto.patch.StoreCategoryPatchDTO;
import com.example.shoppingapi.dto.put.StoreCategoryPutDTO;
import com.example.shoppingapi.model.Store;
import com.example.shoppingapi.model.StoreCategory;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.StoreCategoryRepository;
import com.example.shoppingapi.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreCategoryServiceTest {

    @Mock private StoreCategoryRepository storeCategoryRepository;
    @Mock private StoreRepository storeRepository;
    @InjectMocks private StoreCategoryService storeCategoryService;

    private final ModelHelper<StoreCategory> helper =  ModelHelperFactory.getModelHelper(StoreCategory.class);
    @Test
    void findAll_returnsAllCategories() {
        List<StoreCategory> categories = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(storeCategoryRepository.findAll()).thenReturn(categories);

        List<StoreCategory> result = storeCategoryService.findAll();

        assertEquals(categories, result);
    }

    @Test
    void getStoreCategoryById_categoryFound_returnsCategory() {
        StoreCategory category = helper.createModel(1);
        category.setCategoryName("Electronics");

        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        StoreCategory result = storeCategoryService.getStoreCategoryById(1L);

        assertEquals(category, result);
    }

    @Test
    void getStoreCategoryById_categoryNotFound_throws() {
        when(storeCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeCategoryService.getStoreCategoryById(2L)
        );

        assertEquals("StoreCategory not found with ID: 2", exception.getMessage());
    }

    @Test
    void save_validCategory_createsCategory() {
        StoreCategoryCreateDTO storeCategoryCreateDTO = new StoreCategoryCreateDTO();
        storeCategoryCreateDTO.setStoreId(1L);
        storeCategoryCreateDTO.setCategoryName("Electronics");

        Store store = new Store();
        store.setStoreId(1L);

        StoreCategory storeCategory = new StoreCategory();
        storeCategory.setCategoryName(storeCategoryCreateDTO.getCategoryName());
        storeCategory.setStore(store);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeCategoryRepository.save(any(StoreCategory.class))).thenReturn(storeCategory);

        StoreCategory createdCategory = storeCategoryService.saveStoreCategory(storeCategoryCreateDTO);

        assertNotNull(createdCategory);
        assertEquals("Electronics", createdCategory.getCategoryName());

        verify(storeRepository).findById(1L);
        verify(storeCategoryRepository).save(storeCategory);
    }

    @Test
    void update_validCategory_updatesCategory() {
        StoreCategory existingCategory = new StoreCategory();
        existingCategory.setCategoryName("Old Category");

        StoreCategoryPutDTO storeCategoryPutDTO = new StoreCategoryPutDTO();
        storeCategoryPutDTO.setCategoryName("Updated Category");

        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(storeCategoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreCategory updatedCategory = storeCategoryService.updateStoreCategory(1L, storeCategoryPutDTO);

        assertEquals("Updated Category", updatedCategory.getCategoryName());
        verify(storeCategoryRepository).save(existingCategory);
    }

    @Test
    void partialUpdate_validCategory_updatesCategory() {
        StoreCategory existingCategory = new StoreCategory();
        existingCategory.setCategoryName("Old Category");

        StoreCategoryPatchDTO storeCategoryPatchDTO = new StoreCategoryPatchDTO();
        storeCategoryPatchDTO.setCategoryName("Patched Category");

        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(storeCategoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreCategory patchedCategory = storeCategoryService.partiallyUpdateStoreCategory(1L, storeCategoryPatchDTO);

        assertEquals("Patched Category", patchedCategory.getCategoryName());
        verify(storeCategoryRepository).save(existingCategory);
    }

    @Test
    void deleteStoreCategoryById_categoryExists_deletesCategory() {
        StoreCategory storeCategory = new StoreCategory();
        storeCategory.setCategoryName("Electronics");

        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.of(storeCategory));

        storeCategoryService.deleteById(1L);

        verify(storeCategoryRepository).delete(storeCategory);
    }

    @Test
    void deleteStoreCategoryById_categoryNotFound_throws() {
        when(storeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> storeCategoryService.deleteById(1L)
        );

        assertEquals("StoreCategory not found with ID: 1", exception.getMessage());
    }
}
