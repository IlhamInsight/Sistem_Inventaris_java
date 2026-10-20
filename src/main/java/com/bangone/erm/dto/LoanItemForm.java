package com.bangone.erm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemForm {
    private String productId;
    private Integer qty;
    private String conditionBefore;
    private String notes;
}