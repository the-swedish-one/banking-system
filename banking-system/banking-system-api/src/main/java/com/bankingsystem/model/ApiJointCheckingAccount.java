package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiJointCheckingAccount extends ApiCheckingAccount {
    private ApiUser secondOwner;

    public ApiJointCheckingAccount(ApiUser owner, ApiUser secondOwner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        super(owner, balance, currency, overdraftLimit);
        this.secondOwner = secondOwner;
    }
}
