package com.bangone.erm.repositories.transaction;

import com.bangone.erm.models.transaction.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {

    boolean existsByEmployee_Id(String employeeId);

    long countByEmployee_Id(String employeeId);

    List<LoanRequest> findByEmployee_Id(String employeeId);

    List<LoanRequest> findByStatus(String status);

    boolean existsByRequestCode(String requestCode);
}