package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.patch.ShipmentPatchDTO;
import com.example.shoppingapi.dto.put.ShipmentPutDTO;
import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository       shipmentRepository;
    private final ShipmentVendorRepository vendorRepository;
    private final OrderRepository          orderRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(ShipmentId id) {
        return shipmentRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Shipment not found with ID: " + id));
    }

    public Shipment saveShipment(Shipment shipment) {
        vendorRepository.findById(shipment.getShipmentVendor().getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        orderRepository.findById(shipment.getOrder().getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return shipmentRepository.save(shipment);
    }

    // Update method for ShipmentService
    public Shipment updateShipment(ShipmentId id, ShipmentPutDTO shipmentPutDTO) {
        Shipment existingShipment = getShipmentById(id);
        ReflectionUtils.doWithFields(ShipmentPutDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(shipmentPutDTO);
            if (value != null) {
                Field shipmentField = ReflectionUtils.findField(Shipment.class, field.getName());
                if (shipmentField != null) {
                    shipmentField.setAccessible(true);
                    shipmentField.set(existingShipment, value);
                }
            }
        });
        return shipmentRepository.save(existingShipment);
    }

    // Partial update method for ShipmentService
    public Shipment partiallyUpdateShipment(ShipmentId id, ShipmentPatchDTO shipmentPatchDTO) {
        Shipment existingShipment = getShipmentById(id);
        ReflectionUtils.doWithFields(ShipmentPatchDTO.class, field -> {
            field.setAccessible(true);
            Object value = field.get(shipmentPatchDTO);
            if (value != null) {
                Field shipmentField = ReflectionUtils.findField(Shipment.class, field.getName());
                if (shipmentField != null) {
                    shipmentField.setAccessible(true);
                    shipmentField.set(existingShipment, value);
                }
            }
        });
        return shipmentRepository.save(existingShipment);
    }

}
