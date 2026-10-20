package com.bangone.erm.repositories.transaction;

import com.bangone.erm.models.transaction.LoanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanDetailRepository extends JpaRepository<LoanDetail, String> {
    List<LoanDetail> findByLoanRequestId(String loanRequestId);
}