package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, ShipmentId> {
    Optional<Shipment> findByOrderOrderId(Long orderId);

}
