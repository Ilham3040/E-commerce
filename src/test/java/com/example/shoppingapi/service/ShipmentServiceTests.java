package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
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
class ShipmentServiceTest {

    @Mock private ShipmentRepository       shipmentRepo;
    @Mock private ShipmentVendorRepository vendorRepo;
    @Mock private OrderRepository          orderRepo;
    @InjectMocks private ShipmentService    service;

    private final ModelHelper<Shipment> helper =
        ModelHelperFactory.getModelHelper(Shipment.class);

    @Test
    void findAll_returnsAllShipments() {
        List<Shipment> list = List.of(helper.createModel(1), helper.createModel(2));
        when(shipmentRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(shipmentRepo).findAll();
    }

    @Test
    void findById_found_returnsShipment() {
        Shipment s = helper.createModel(1);
        ShipmentId id = s.getId();
        when(shipmentRepo.findById(id)).thenReturn(Optional.of(s));

        assertEquals(s, service.findById(id));
        verify(shipmentRepo).findById(id);
    }

    @Test
    void findById_notFound_throwsException() {
        ShipmentId id = ShipmentId.builder().vendorId(9L).orderId(9L).build();
        when(shipmentRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(id)
        );
        assertEquals("Shipment not found with ID: " + id, ex.getMessage());
    }

    @Test
    void saveShipment_success_savesAndReturnsShipment() {
        Shipment s = helper.createModel(1);
        when(vendorRepo.findById(s.getShipmentVendor().getVendorId()))
            .thenReturn(Optional.of(s.getShipmentVendor()));
        when(orderRepo.findById(s.getOrder().getOrderId()))
            .thenReturn(Optional.of(s.getOrder()));
        when(shipmentRepo.save(any())).thenReturn(s);

        assertEquals(s, service.saveShipment(s));
        verify(shipmentRepo).save(s);
    }

    @Test
    void saveShipment_vendorNotFound_throwsException() {
        Shipment s = helper.createModel(1);
        when(vendorRepo.findById(s.getShipmentVendor().getVendorId()))
            .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.saveShipment(s)
        );
        assertEquals("Vendor not found", ex.getMessage());
        verify(shipmentRepo, never()).save(any());
    }

    @Test
    void saveShipment_orderNotFound_throwsException() {
        Shipment s = helper.createModel(1);
        when(vendorRepo.findById(s.getShipmentVendor().getVendorId()))
            .thenReturn(Optional.of(s.getShipmentVendor()));
        when(orderRepo.findById(s.getOrder().getOrderId()))
            .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.saveShipment(s)
        );
        assertEquals("Order not found", ex.getMessage());
        verify(shipmentRepo, never()).save(any());
    }
}
