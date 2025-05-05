package com.example.shoppingapi.controller;

import com.example.shoppingapi.EntityCreationHelper;
import com.example.shoppingapi.model.*;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityCreationHelper entityCreationHelper;

    @Test
    public void testCreateShipment() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);
        ShipmentVendor createdVendor = entityCreationHelper.createShipmentVendor();

        String shipmentCreateJson = "{\n" +
                "  \"orderId\": " + createdOrder.getOrderId() + ",\n" +
                "  \"vendorId\":" + createdVendor.getVendorId() +  "\n" +
                "}";

        mockMvc.perform(post("/api/shipments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shipmentCreateJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created shipment"))
                .andExpect(jsonPath("$.data.orderId").value(createdOrder.getOrderId()))
                .andExpect(jsonPath("$.data.vendorId").value(createdVendor.getVendorId()));
    }

    @Test
    public void testGetAllShipments() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);
        ShipmentVendor createdVendor = entityCreationHelper.createShipmentVendor();
        Shipment createdShipment = entityCreationHelper.createShipment(createdOrder, createdVendor.getVendorId());

        mockMvc.perform(get("/api/shipments/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched all shipments"))
                .andExpect(jsonPath("$.data[0].orderId").value(createdShipment.getOrder().getOrderId()))
                .andExpect(jsonPath("$.data[0].vendorId").value(createdShipment.getShipmentVendor().getVendorId()));
    }

    @Test
    public void testGetShipmentByOrderId() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);
        ShipmentVendor createdVendor = entityCreationHelper.createShipmentVendor();
        Shipment createdShipment = entityCreationHelper.createShipment(createdOrder, createdVendor.getVendorId());

        mockMvc.perform(get("/api/shipments/order/{id}", createdOrder.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully fetched shipment"))
                .andExpect(jsonPath("$.data.orderId").value(createdOrder.getOrderId()))
                .andExpect(jsonPath("$.data.vendorId").value(createdVendor.getVendorId()));
    }

    @Test
    public void testUpdateShipment() throws Exception {
        User createdUser = entityCreationHelper.createUser();
        Store createdStore = entityCreationHelper.createStore(createdUser);
        Product createdProduct = entityCreationHelper.createProduct(createdStore);
        Order createdOrder = entityCreationHelper.createOrder(createdUser,createdProduct);
        ShipmentVendor createdVendor = entityCreationHelper.createShipmentVendor();
        Shipment createdShipment = entityCreationHelper.createShipment(createdOrder, createdVendor.getVendorId());

        String shipmentPutJson = "{\n" +
                "  \"shipmentStatus\": 5\n" +
                "}";

        mockMvc.perform(put("/api/shipments/{vendorId}/{orderId}", createdVendor.getVendorId(), createdOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shipmentPutJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated shipment"))
                .andExpect(jsonPath("$.data.orderId").value(createdOrder.getOrderId()))
                .andExpect(jsonPath("$.data.vendorId").value(createdVendor.getVendorId()));
    }
}
