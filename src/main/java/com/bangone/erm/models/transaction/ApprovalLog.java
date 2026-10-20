package com.bangone.erm.models.transaction;

import com.bangone.erm.models.BaseEntity;
import com.bangone.erm.models.auth.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "approval_logs")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalLog extends BaseEntity {
    @ManyToOne
    private LoanRequest loanRequest;
    
    @ManyToOne
    private User approver; // Manager yang setuju
    
    private String approvalStatus;
    private String comments;
}