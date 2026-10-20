package com.bangone.erm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestForm {
    private String id;
    private String employeeId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private String purpose;
    private String notes;
    private String status;
    private List<LoanItemForm> items = new ArrayList<>();
}