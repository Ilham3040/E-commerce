package com.example.shoppingapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.ShipmentVendorRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ShipmentVendorServiceTests.class)
public class ShipmentVendorServiceTests {

    @Mock
    private ShipmentVendorRepository shipmentVendorRepository;

    @InjectMocks
    private ShipmentVendorService shipmentVendorService;

    private ModelHelper<ShipmentVendor> shipmentVendorHelper = ModelHelperFactory.getModelHelper(ShipmentVendor.class);

    @Test
    public void testGetAllShipmentVendors() {
        ShipmentVendor shipmentVendor1 = shipmentVendorHelper.createModel(1);
        ShipmentVendor shipmentVendor2 = shipmentVendorHelper.createModel(2);
    
        List<ShipmentVendor> mockShipmentVendors = Arrays.asList(shipmentVendor1, shipmentVendor2);
    
        when(shipmentVendorRepository.findAll()).thenReturn(mockShipmentVendors);
    
        List<ShipmentVendor> result = shipmentVendorService.findAll();
    
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockShipmentVendors, result);

        verify(shipmentVendorRepository, times(1)).findAll();
    }

    @Test
    public void testCreateShipmentVendor() {
        ShipmentVendor shipmentVendor = shipmentVendorHelper.createModel(1);

        when(shipmentVendorRepository.save(any(ShipmentVendor.class))).thenReturn(shipmentVendor);

        ShipmentVendor createdShipmentVendor = shipmentVendorService.saveShipmentVendor(shipmentVendor);

        assertNotNull(createdShipmentVendor);
        assertEquals(shipmentVendor, createdShipmentVendor);

        verify(shipmentVendorRepository, times(1)).save(shipmentVendor);
    }

    @Test
    public void testGetShipmentVendorById() {
        ShipmentVendor shipmentVendor = shipmentVendorHelper.createModel(1);
        when(shipmentVendorRepository.findById(shipmentVendor.getVendorId())).thenReturn(Optional.of(shipmentVendor));

        ShipmentVendor result = shipmentVendorService.findById(shipmentVendor.getVendorId()).orElseThrow(() -> new AssertionError("ShipmentVendor not found"));

        assertNotNull(result);
        assertEquals(shipmentVendor, result);

        verify(shipmentVendorRepository, times(1)).findById(shipmentVendor.getVendorId());
    }

    @Test
    public void testGetShipmentVendorById_NotFound() {
        when(shipmentVendorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ShipmentVendor> result = shipmentVendorService.findById(1L);

        assertFalse(result.isPresent());
        verify(shipmentVendorRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateShipmentVendor() {
        ShipmentVendor existing = shipmentVendorHelper.createModel(1);
        
        when(shipmentVendorRepository.findById(existing.getVendorId())).thenReturn(Optional.of(existing));
        when(shipmentVendorRepository.save(any(ShipmentVendor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        ShipmentVendor updated = existing.toBuilder().vendorContact("0987654321").build();
        ShipmentVendor result = shipmentVendorService.updateShipmentVendor(existing.getVendorId(), updated);

        assertNotNull(result);
        assertEquals(updated, result);

        verify(shipmentVendorRepository, times(1)).findById(existing.getVendorId());
        verify(shipmentVendorRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateShipmentVendor() {
        ShipmentVendor existing = shipmentVendorHelper.createModel(1);

        when(shipmentVendorRepository.findById(existing.getVendorId())).thenReturn(Optional.of(existing));
        when(shipmentVendorRepository.save(any(ShipmentVendor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of(
            "vendorEmail", "heyguys@example.com",
            "vendorContact", "0987654321"
        );
        ShipmentVendor result = shipmentVendorService.partialUpdateShipmentVendor(existing.getVendorId(), updates);

        assertNotNull(result);
        assertEquals("heyguys@example.com", result.getVendorEmail());
        assertEquals("0987654321", result.getVendorContact());
        
        verify(shipmentVendorRepository, times(1)).findById(existing.getVendorId());
        verify(shipmentVendorRepository, times(1)).save(existing);
    }

    @Test
    public void testSoftDeleteById() {
        ShipmentVendor shipmentVendor = shipmentVendorHelper.createModel(1);
        
        when(shipmentVendorRepository.findById(shipmentVendor.getVendorId())).thenReturn(Optional.of(shipmentVendor));
        when(shipmentVendorRepository.save(any(ShipmentVendor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShipmentVendor deletedShipmentVendor = shipmentVendorService.deleteById(shipmentVendor.getVendorId());

        assertNotNull(deletedShipmentVendor);
        assertNotNull(deletedShipmentVendor.getDeletedAt());
        assertEquals(shipmentVendor.getVendorId(), deletedShipmentVendor.getVendorId());

        verify(shipmentVendorRepository, times(1)).save(deletedShipmentVendor);
        verify(shipmentVendorRepository, times(1)).findById(shipmentVendor.getVendorId());
    }

    @Test
    public void testSoftDeleteById_NotFound() {
        when(shipmentVendorRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            shipmentVendorService.deleteById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Shipment Vendor not found with ID: 1", e.getMessage());
        }

        verify(shipmentVendorRepository, times(1)).findById(1L);
    }
}
