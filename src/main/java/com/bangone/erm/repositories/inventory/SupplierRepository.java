package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    boolean existsByCode(String code);
    Optional<Supplier> findByCode(String code);
}