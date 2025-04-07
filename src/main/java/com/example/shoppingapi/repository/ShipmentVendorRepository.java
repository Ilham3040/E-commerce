package com.example.shoppingapi.repository;

import com.example.shoppingapi.model.ShipmentVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentVendorRepository extends JpaRepository<ShipmentVendor, Long> {
}
