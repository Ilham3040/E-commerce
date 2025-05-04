package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.ShipmentCreateDTO;
import com.example.shoppingapi.dto.patch.ShipmentPatchDTO;
import com.example.shoppingapi.dto.put.ShipmentPutDTO;
import com.example.shoppingapi.model.Order;
import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;
import com.example.shoppingapi.model.ShipmentVendor;
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

    public Shipment getShipmentByOrderId(Long id) {
        return shipmentRepository.findByOrderOrderId(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Shipment not found for order with ID: " + id));
    }

    public Shipment getShipmentById(ShipmentId id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shipment not found for order with ID: " + id));
    }

    public Shipment saveShipment(ShipmentCreateDTO shipmentCreateDTO) {
        vendorRepository.findById(shipmentCreateDTO.getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        orderRepository.findById(shipmentCreateDTO.getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        ShipmentId shipmentId = ShipmentId.builder().orderId(shipmentCreateDTO.getOrderId()).vendorId(shipmentCreateDTO.getVendorId()).build();

        Shipment shipment = Shipment.builder()
                .id(shipmentId)
                .shipmentVendor(ShipmentVendor.builder().vendorId(shipmentCreateDTO.getVendorId()).build())
                .order(Order.builder().orderId(shipmentCreateDTO.getOrderId()).build())
                .build();

        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(ShipmentId id, ShipmentPutDTO shipmentPutDTO) {
        Shipment existingShipment = getShipmentById(id);
        existingShipment.setShipmentStatus(shipmentPutDTO.getShipmentStatus());
        return shipmentRepository.save(existingShipment);
    }

}
