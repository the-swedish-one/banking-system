package com.bankingsystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private Integer id;
    private String bankName;
    private String bic;
    private BigDecimal collectedInterest = BigDecimal.ZERO;

    public Bank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

    public void addCollectedInterest(BigDecimal amount) {
        collectedInterest = collectedInterest.add(amount);
    }
}
