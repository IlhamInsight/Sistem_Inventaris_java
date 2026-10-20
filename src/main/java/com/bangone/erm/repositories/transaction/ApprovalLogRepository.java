package com.bangone.erm.repositories.transaction;

import com.bangone.erm.models.transaction.ApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLogRepository extends JpaRepository<ApprovalLog, String> {
}