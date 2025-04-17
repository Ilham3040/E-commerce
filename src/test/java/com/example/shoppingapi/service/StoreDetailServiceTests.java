package com.example.shoppingapi.service;

import com.example.shoppingapi.model.StoreDetail;
import com.example.shoppingapi.repository.StoreDetailRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StoreDetailServiceTests {

    @Mock
    private StoreDetailRepository storeDetailRepository;

    @InjectMocks
    private StoreDetailService storeDetailService;

    private ModelHelper<StoreDetail> storeDetailHelper = ModelHelperFactory.getModelHelper(StoreDetail.class);

    @Test
    public void testFindAll() {
        StoreDetail storeDetail1 = storeDetailHelper.createModel(1);
        StoreDetail storeDetail2 = storeDetailHelper.createModel(2);

        when(storeDetailRepository.findAll()).thenReturn(Arrays.asList(storeDetail1, storeDetail2));

        var result = storeDetailService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Arrays.asList(storeDetail1, storeDetail2), result);

        verify(storeDetailRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        StoreDetail storeDetail = storeDetailHelper.createModel(1);
        when(storeDetailRepository.findById(storeDetail.getStoreDetailId())).thenReturn(Optional.of(storeDetail));

        StoreDetail result = storeDetailService.findById(storeDetail.getStoreDetailId()).orElseThrow(() -> new AssertionError("StoreDetail not found"));

        assertNotNull(result);
        assertEquals(storeDetail, result);

        verify(storeDetailRepository, times(1)).findById(storeDetail.getStoreDetailId());
    }

    @Test
    public void testFindById_NotFound() {
        when(storeDetailRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StoreDetail> result = storeDetailService.findById(1L);

        assertFalse(result.isPresent());

        verify(storeDetailRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveStoreDetail() {
        StoreDetail storeDetail = storeDetailHelper.createModel(1);

        when(storeDetailRepository.save(any(StoreDetail.class))).thenReturn(storeDetail);

        StoreDetail createdStoreDetail = storeDetailService.saveStoreDetail(storeDetail);

        assertNotNull(createdStoreDetail);
        assertEquals(storeDetail, createdStoreDetail);

        verify(storeDetailRepository, times(1)).save(storeDetail);
    }

    @Test
    public void testUpdateStoreDetail() {
        StoreDetail existing = storeDetailHelper.createModel(1);
        
        when(storeDetailRepository.findById(existing.getStoreDetailId())).thenReturn(Optional.of(existing));
        when(storeDetailRepository.save(any(StoreDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StoreDetail updated = existing.toBuilder().address("New Address").build();
        StoreDetail result = storeDetailService.updateStoreDetail(existing.getStoreDetailId(), updated);

        assertNotNull(result);
        assertEquals(updated, result);

        verify(storeDetailRepository, times(1)).findById(existing.getStoreDetailId());
        verify(storeDetailRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateStoreDetail() {
        StoreDetail existing = storeDetailHelper.createModel(1);

        when(storeDetailRepository.findById(existing.getStoreDetailId())).thenReturn(Optional.of(existing));
        when(storeDetailRepository.save(any(StoreDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of(
            "address", "Updated Address",
            "followerCount", 50
        );
        StoreDetail result = storeDetailService.partialUpdateStoreDetail(existing.getStoreDetailId(), updates);

        assertNotNull(result);
        assertEquals("Updated Address", result.getAddress());
        assertEquals(50, result.getFollowerCount());

        verify(storeDetailRepository, times(1)).findById(existing.getStoreDetailId());
        verify(storeDetailRepository, times(1)).save(existing);
    }


    @Test
    public void testDeleteStoreDetail() {
        StoreDetail storeDetail = storeDetailHelper.createModel(1);

        when(storeDetailRepository.findById(storeDetail.getStoreDetailId())).thenReturn(Optional.of(storeDetail));

        storeDetailService.deleteById(storeDetail.getStoreDetailId());

        verify(storeDetailRepository, times(1)).findById(storeDetail.getStoreDetailId());
    }

    @Test
    public void testDeleteStoreDetail_NotFound() {
        when(storeDetailRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            storeDetailService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("StoreDetail not found with ID: 1", e.getMessage());
        }

        verify(storeDetailRepository, times(1)).findById(1L);
    }
}
