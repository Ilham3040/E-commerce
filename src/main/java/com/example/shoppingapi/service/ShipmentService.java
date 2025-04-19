package com.example.shoppingapi.service;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.repository.ShipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.example.shoppingapi.repository.OrderRepository;
import com.example.shoppingapi.repository.ShipmentVendorRepository;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentVendorRepository shipmentVendorRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> findById(ShipmentId id) {
        return shipmentRepository.findById(id);
    }

    public Shipment saveShipment(Shipment shipment) {
        shipmentVendorRepository.findById(shipment.getShipmentVendor().getVendorId())
        .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        orderRepository.findById(shipment.getOrder().getOrderId())
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(ShipmentId id, Shipment shipment) {
        Optional<Shipment> existingOpt = shipmentRepository.findById(id);

        if (shipment.getId() != id){
            throw new IllegalArgumentException("id parameter and id in body must match");
        }

        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Shipment not found with ID: " + id);
        }

        shipmentVendorRepository.findById(shipment.getShipmentVendor().getVendorId())
        .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        orderRepository.findById(shipment.getOrder().getOrderId())
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        shipment.setId(id);
        return shipmentRepository.save(shipment);
    }

    public Shipment partialUpdateShipment(ShipmentId id, Map<String, Object> updates) {
        Optional<Shipment> existingOpt = shipmentRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Shipment not found with ID: " + id);
        }
        Shipment shipment = existingOpt.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Shipment.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, shipment, value);
            }
        });
        return shipmentRepository.save(shipment);
    }

}
