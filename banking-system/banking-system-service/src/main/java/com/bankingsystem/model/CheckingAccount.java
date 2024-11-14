package com.bankingsystem.model;

import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount extends Account {

    private BigDecimal overdraftLimit;

    public CheckingAccount(User owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        super(owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw Failed: Amount must be greater than 0");
        }

        BigDecimal availableBalance = this.balance.add(this.overdraftLimit);
        if (availableBalance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new OverdraftLimitExceededException("Withdraw Failed: Overdraft limit exceeded");
        }
    }
}
