package com.example.shoppingapi.service;

import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ShipmentServiceTests.class)
public class ShipmentServiceTests {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentVendorRepository shipmentVendorRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private final ModelHelper<Shipment> shipmentHelper = ModelHelperFactory.getModelHelper(Shipment.class);

    @Test
    public void testFindAll() {
        Shipment shipment1 = shipmentHelper.createModel(1);
        Shipment shipment2 = shipmentHelper.createModel(2);

        List<Shipment> mockList = Arrays.asList(shipment1, shipment2);

        when(shipmentRepository.findAll()).thenReturn(mockList);

        List<Shipment> result = shipmentService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(shipment1, result.get(0));
        assertEquals(shipment2, result.get(1));

        verify(shipmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Shipment shipment = shipmentHelper.createModel(1);
        ShipmentId id = shipment.getId();
        when(shipmentRepository.findById(id))
            .thenReturn(Optional.of(shipment));
        Shipment result = shipmentService.findById(id).orElseThrow(() -> new AssertionError("Shipment doesn't exist"));
        assertNotNull(result);
        assertEquals(shipment, result);

        verify(shipmentRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        ShipmentId missingId = ShipmentId.builder().vendorId(9L).orderId(9L).build();

        when(shipmentRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<Shipment> result = shipmentService.findById(missingId);

        assertFalse(result.isPresent());

        verify(shipmentRepository, times(1)).findById(missingId);
    }

    @Test
    public void testSaveShipment() {
        Shipment shipment = shipmentHelper.createModel(1);

        when(orderRepository.findById(shipment.getOrder().getOrderId()))
            .thenReturn(Optional.of(shipment.getOrder()));
        when(shipmentVendorRepository.findById(shipment.getShipmentVendor().getVendorId()))
            .thenReturn(Optional.of(shipment.getShipmentVendor()));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        Shipment created = shipmentService.saveShipment(shipment);
        
        assertNotNull(created);
        assertEquals(shipment, created);

        verify(shipmentRepository, times(1)).save(shipment);
        verify(shipmentVendorRepository, times(1)).findById(shipment.getShipmentVendor().getVendorId());
        verify(orderRepository, times(1)).findById(shipment.getOrder().getOrderId());
    }

    // @Test
    // public void testUpdateShipment() {
    //     Shipment shipment = shipmentHelper.createModel(1);
    //     ShipmentId id = shipment.getId();

    //     when(orderRepository.findById(shipment.getOrder().getOrderId()))
    //         .thenReturn(Optional.of(shipment.getOrder()));
    //     when(shipmentVendorRepository.findById(shipment.getShipmentVendor().getVendorId()))
    //         .thenReturn(Optional.of(shipment.getShipmentVendor()));
    //     when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
    //     when(shipmentRepository.findById(id)).thenReturn(Optional.of(shipment));


    //     Shipment updated  = shipment.toBuilder().updatedAt(LocalDateTime.now()).build();
    //     Shipment result = shipmentService.updateShipment(shipment.getId(), updated);

    //     assertNotNull(result);
    //     assertEquals(updated, result);
        
    //     verify(shipmentRepository, times(1)).save(updated);
    //     verify(shipmentVendorRepository, times(1)).findById(shipment.getShipmentVendor().getVendorId());
    //     verify(orderRepository, times(1)).findById(shipment.getOrder().getOrderId());
    //     verify(shipmentRepository, times(1)).findById(id);
    // }

    // @Test
    // public void testPartialUpdateShipment() {
    //     Shipment shipment = shipmentHelper.createModel(2);
    //     ShipmentId id = shipment.getId();

    //     when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
    //     when(shipmentRepository.findById(id)).thenReturn(Optional.of(shipment));

    //     LocalDateTime now = LocalDateTime.now();
    //     Map<String, Object> updates = Map.of("updatedAt", now);
    //     Shipment result = shipmentService.partialUpdateShipment(id, updates);

    //     assertNotNull(result);
    //     assertEquals(now, result.getUpdatedAt());

    //     verify(shipmentRepository, times(1)).save(shipment);
    //     verify(shipmentRepository, times(1)).findById(id);
    }