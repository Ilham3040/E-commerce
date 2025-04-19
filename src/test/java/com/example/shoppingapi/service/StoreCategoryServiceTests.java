package com.example.shoppingapi.service;

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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreCategoryServiceTest {

    @Mock private StoreCategoryRepository categoryRepo;
    @Mock private StoreRepository         storeRepo;
    @InjectMocks private StoreCategoryService service;

    private final ModelHelper<StoreCategory> helper =
        ModelHelperFactory.getModelHelper(StoreCategory.class);

    @Test
    void findAll_returnsAll() {
        List<StoreCategory> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(categoryRepo.findAll()).thenReturn(list);
        assertEquals(list, service.findAll());
    }

    @Test
    void findById_found_returnsCategory() {
        StoreCategory c = helper.createModel(1);
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(c));
        assertEquals(c, service.findById(1L));
    }

    @Test
    void findById_notFound_throws() {
        when(categoryRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("StoreCategory not found with ID: 2", ex.getMessage());
    }

    @Test
    void save_valid_savesAndReturns() {
        StoreCategory c = helper.createModel(1);
        when(storeRepo.findById(c.getStore().getStoreId()))
            .thenReturn(Optional.of(c.getStore()));
        when(categoryRepo.save(c)).thenReturn(c);
        assertEquals(c, service.saveStoreCategory(c));
    }

    @Test
    void update_valid_savesUpdated() {
        StoreCategory c = helper.createModel(1);
        StoreCategory upd = c.toBuilder().categoryName("X").build();
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(c));
        when(storeRepo.findById(c.getStore().getStoreId()))
            .thenReturn(Optional.of(c.getStore()));
        when(categoryRepo.save(any())).thenReturn(upd);
        assertEquals(upd, service.updateStoreCategory(1L, upd));
    }

    @Test
    void partialUpdate_appliesUpdates() {
        StoreCategory c = helper.createModel(1);
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(c));
        when(categoryRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        StoreCategory res = service.partialUpdateStoreCategory(1L, Map.of("categoryName","Y"));
        assertEquals("Y", res.getCategoryName());
    }

    @Test
    void softDelete_setsDeletedAt() {
        StoreCategory c = helper.createModel(1);
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(c));
        when(categoryRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        StoreCategory res = service.softDeleteStoreCategory(1L);
        assertNotNull(res.getDeletedAt());
    }
}
