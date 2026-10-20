package com.bangone.erm.models.transaction;

import com.bangone.erm.models.BaseEntity;
import com.bangone.erm.models.auth.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "return_transactions")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "loan_request_id", nullable = false)
    private LoanRequest loanRequest;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy; // 🔥 TAMBAHAN

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "condition_after", length = 100)
    private String conditionAfter;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 30)
    private String status;
}