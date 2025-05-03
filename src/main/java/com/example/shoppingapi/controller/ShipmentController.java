//package com.example.shoppingapi.controller;
//
//import com.example.shoppingapi.dto.create.ShipmentCreateDTO;
//import com.example.shoppingapi.dto.put.ShipmentPutDTO;
//import com.example.shoppingapi.dto.patch.ShipmentPatchDTO;
//import com.example.shoppingapi.dto.response.ApiResponse;
//import com.example.shoppingapi.dto.response.ShipmentDTO;
//import com.example.shoppingapi.model.Shipment;
//import com.example.shoppingapi.model.ShipmentId;
//import com.example.shoppingapi.service.ShipmentService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/shipments")
//@RequiredArgsConstructor
//public class ShipmentController {
//    private final ShipmentService shipmentService;
//
//    @GetMapping
//    public ApiResponse<List<ShipmentDTO>> getAllShipments() {
//        List<ShipmentDTO> shipmentDTOs = shipmentService.findAll()
//                .stream()
//                .map(shipment -> new ShipmentDTO(shipment.getOrder().getOrderId(), shipment.getShipmentVendor().getVendorId()))
//                .collect(Collectors.toList());
//        return new ApiResponse<>("Successfully fetched all shipments", shipmentDTOs, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<ShipmentDTO> getShipmentById(@PathVariable ShipmentId id) {
//        Shipment shipment = shipmentService.getShipmentById(id);
//        return new ApiResponse<>("Successfully fetched shipment", new ShipmentDTO(shipment.getOrder().getOrderId(), shipment.getShipmentVendor().getVendorId()), HttpStatus.OK);
//    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public ApiResponse<ShipmentDTO> createShipment(@Validated @RequestBody ShipmentCreateDTO shipmentCreateDTO) {
//        Shipment createdShipment = shipmentService.saveShipment(shipmentCreateDTO);
//        return new ApiResponse<>("Successfully created shipment", new ShipmentDTO(createdShipment.getOrder().getOrderId(), createdShipment.getShipmentVendor().getVendorId()), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<ShipmentDTO> updateShipment(@PathVariable ShipmentId id, @Validated @RequestBody ShipmentPutDTO shipmentPutDTO) {
//        Shipment updatedShipment = shipmentService.updateShipment(id, shipmentPutDTO);
//        return new ApiResponse<>("Successfully updated shipment", new ShipmentDTO(updatedShipment.getOrder().getOrderId(), updatedShipment.getShipmentVendor().getVendorId()), HttpStatus.OK);
//    }
//}
