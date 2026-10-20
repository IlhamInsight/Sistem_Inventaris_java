package com.bangone.erm.repositories.transaction;

import com.bangone.erm.models.transaction.ReturnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnTransactionRepository extends JpaRepository<ReturnTransaction, String> {

    boolean existsByLoanRequestId(String loanRequestId);

    List<ReturnTransaction> findByCreatedById(String userId); // 🔥 TAMBAHAN
}