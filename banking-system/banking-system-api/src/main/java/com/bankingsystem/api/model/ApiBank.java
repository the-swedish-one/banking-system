package com.bankingsystem.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBank {

    private Integer id;
    private String bankName;
    private String bic;
    private BigDecimal collectedInterest;

    public ApiBank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

}
