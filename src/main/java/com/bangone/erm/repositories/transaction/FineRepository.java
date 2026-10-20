package com.bangone.erm.repositories.transaction;

import com.bangone.erm.models.transaction.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<Fine, String> {
}