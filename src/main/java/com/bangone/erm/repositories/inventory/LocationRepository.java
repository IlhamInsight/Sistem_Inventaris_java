package com.bangone.erm.repositories.inventory;

import com.bangone.erm.models.inventory.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
}