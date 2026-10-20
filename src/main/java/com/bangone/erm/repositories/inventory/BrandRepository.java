package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
}