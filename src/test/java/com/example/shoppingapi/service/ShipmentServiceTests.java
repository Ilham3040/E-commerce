package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ShipmentCreateDTO;
import com.example.shoppingapi.dto.put.ShipmentPutDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.model.ShipmentVendor;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
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
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository       shipmentRepository;
    @Mock
    private ShipmentVendorRepository vendorRepository;
    @Mock
    private OrderRepository          orderRepository;
    @InjectMocks
    private ShipmentService          service;

    @Test
    void findAll_returnsAllShipments() {
        List<Shipment> shipments = Arrays.asList(
                new Shipment(), new Shipment()
        );
        when(shipmentRepository.findAll()).thenReturn(shipments);

        List<Shipment> result = service.findAll();

        assertEquals(shipments, result);
        verify(shipmentRepository).findAll();
    }

    @Test
    void getShipmentById_found_returnsShipment() {
        ShipmentId shipmentId = new ShipmentId(1L, 2L);
        Shipment existingShipment = new Shipment();
        existingShipment.setId(shipmentId);

        when(shipmentRepository.findById(shipmentId))
                .thenReturn(Optional.of(existingShipment));

        Shipment result = service.getShipmentById(shipmentId);

        assertSame(existingShipment, result);
        verify(shipmentRepository).findById(shipmentId);
    }

    @Test
    void getShipmentById_notFound_throwsResourceNotFound() {
        ShipmentId missingId = new ShipmentId(5L, 6L);
        when(shipmentRepository.findById(missingId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getShipmentById(missingId)
        );
        assertEquals("Shipment not found with ID: " + missingId, ex.getMessage());
    }

    @Test
    void saveShipment_validData_savesAndReturnsShipment() {
        ShipmentCreateDTO createDTO = new ShipmentCreateDTO();
        createDTO.setOrderId(10L);
        createDTO.setVendorId(20L);

        when(vendorRepository.findById(20L))
                .thenReturn(Optional.of(ShipmentVendor.builder().vendorId(20L).build()));
        when(orderRepository.findById(10L))
                .thenReturn(Optional.of(Order.builder().orderId(10L).build()));
        when(shipmentRepository.save(any(Shipment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Shipment result = service.saveShipment(createDTO);

        assertNotNull(result);
        assertEquals(20L, result.getShipmentVendor().getVendorId());
        assertEquals(10L, result.getOrder().getOrderId());
        verify(vendorRepository).findById(20L);
        verify(orderRepository).findById(10L);
        verify(shipmentRepository).save(result);
    }

    @Test
    void saveShipment_vendorNotFound_throwsResourceNotFound() {
        ShipmentCreateDTO createDTO = new ShipmentCreateDTO();
        createDTO.setOrderId(10L);
        createDTO.setVendorId(20L);

        when(vendorRepository.findById(20L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveShipment(createDTO)
        );
        assertEquals("Vendor not found", ex.getMessage());
        verify(vendorRepository).findById(20L);
        verifyNoInteractions(orderRepository, shipmentRepository);
    }

    @Test
    void saveShipment_orderNotFound_throwsResourceNotFound() {
        ShipmentCreateDTO createDTO = new ShipmentCreateDTO();
        createDTO.setOrderId(10L);
        createDTO.setVendorId(20L);

        when(vendorRepository.findById(20L))
                .thenReturn(Optional.of(ShipmentVendor.builder().vendorId(20L).build()));
        when(orderRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveShipment(createDTO)
        );
        assertEquals("Order not found", ex.getMessage());
        verify(vendorRepository).findById(20L);
        verify(orderRepository).findById(10L);
        verifyNoInteractions(shipmentRepository);
    }

    @Test
    void updateShipment_valid_updatesStatus() {
        ShipmentId shipmentId = new ShipmentId(3L, 4L);
        Shipment existing = new Shipment();
        existing.setId(shipmentId);
        existing.setShipmentStatus(1);

        ShipmentPutDTO putDTO = new ShipmentPutDTO();
        putDTO.setShipmentStatus(7);

        when(shipmentRepository.findById(shipmentId))
                .thenReturn(Optional.of(existing));
        when(shipmentRepository.save(existing))
                .thenReturn(existing);

        Shipment updated = service.updateShipment(shipmentId, putDTO);

        assertEquals(7, updated.getShipmentStatus());
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).save(existing);
    }
}
