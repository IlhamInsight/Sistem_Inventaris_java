package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    long countByCategoryId(String categoryId);

    @Query(value = "SELECT COUNT(*) FROM products WHERE brand_id = :brandId", nativeQuery = true)
    long countProductsByBrandId(@Param("brandId") String brandId);
}