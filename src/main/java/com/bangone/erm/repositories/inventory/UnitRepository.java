package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, String> {
}