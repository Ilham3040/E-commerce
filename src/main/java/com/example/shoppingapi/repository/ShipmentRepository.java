package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.Shipment;
import com.example.shoppingapi.model.ShipmentId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, ShipmentId> {
}
