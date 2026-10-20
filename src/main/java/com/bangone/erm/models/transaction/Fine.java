package com.bangone.erm.models.transaction;

import com.bangone.erm.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "fines")
@Data
@EqualsAndHashCode(callSuper = true)
public class Fine extends BaseEntity {
    @ManyToOne
    private LoanRequest loanRequest;
    
    private Double amount; // Jumlah denda
    private String reason; // Alasannya: Rusak/Telat
    private Boolean isPaid = false;
}