package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JointCheckingAccount extends CheckingAccount {
    private User secondOwner;

    public JointCheckingAccount(User owner, User secondOwner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        super(owner, balance, currency, overdraftLimit);
        this.secondOwner = secondOwner;
    }
}
