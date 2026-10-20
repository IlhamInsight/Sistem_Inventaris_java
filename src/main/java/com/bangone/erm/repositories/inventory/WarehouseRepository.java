package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
    boolean existsByCode(String code);
    Optional<Warehouse> findByCode(String code);
}