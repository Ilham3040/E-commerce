package com.example.shoppingapi.service;

import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentVendorServiceTest {

    @Mock private ShipmentVendorRepository vendorRepo;
    @InjectMocks private ShipmentVendorService service;

    private final ModelHelper<ShipmentVendor> helper =
        ModelHelperFactory.getModelHelper(ShipmentVendor.class);

    @Test
    void findAll_returnsAllVendors() {
        List<ShipmentVendor> list = List.of(helper.createModel(1), helper.createModel(2));
        when(vendorRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(vendorRepo).findAll();
    }

    @Test
    void findById_found_returnsVendor() {
        ShipmentVendor v = helper.createModel(1);
        when(vendorRepo.findById(1L)).thenReturn(Optional.of(v));

        assertEquals(v, service.findById(1L));
    }

    @Test
    void findById_notFound_throwsException() {
        when(vendorRepo.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(2L)
        );
        assertEquals("ShipmentVendor not found with ID: 2", ex.getMessage());
    }

    @Test
    void saveShipmentVendor_savesAndReturns() {
        ShipmentVendor v = helper.createModel(1);
        when(vendorRepo.save(any())).thenReturn(v);

        assertEquals(v, service.saveShipmentVendor(v));
    }

    @Test
    void updateShipmentVendor_success_savesUpdated() {
        ShipmentVendor orig = helper.createModel(1);
        ShipmentVendor upd  = orig.toBuilder().vendorContact("123").build();
        when(vendorRepo.findById(1L)).thenReturn(Optional.of(orig));
        when(vendorRepo.save(any())).thenReturn(upd);

        ShipmentVendor res = service.updateShipmentVendor(1L, upd);
        assertEquals("123", res.getVendorContact());
        verify(vendorRepo).save(upd);
    }

    @Test
    void partialUpdateShipmentVendor_appliesUpdates() {
        ShipmentVendor v = helper.createModel(1);
        when(vendorRepo.findById(1L)).thenReturn(Optional.of(v));
        when(vendorRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Map<String,Object> changes = Map.of(
            "vendorEmail","x@y.com",
            "vendorContact","000"
        );
        ShipmentVendor res = service.partialUpdateShipmentVendor(1L, changes);
        assertEquals("x@y.com", res.getVendorEmail());
        assertEquals("000",    res.getVendorContact());
    }

    @Test
    void softDeleteShipmentVendor_setsDeletedAt() {
        ShipmentVendor v = helper.createModel(1);
        when(vendorRepo.findById(1L)).thenReturn(Optional.of(v));
        when(vendorRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        ShipmentVendor res = service.softDeleteShipmentVendor(1L);
        assertNotNull(res.getDeletedAt());
        assertTrue(res.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
