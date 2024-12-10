package com.bankingsystem.domain.model;

import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.service.CheckingAccountService;
import com.bankingsystem.persistence.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount implements Withdrawable, Depositable  {
    private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

    private int accountId;
    private String iban;
    private String accountName;
    private BigDecimal balance;
    private CurrencyCode currency;
    private BigDecimal overdraftLimit;
    private Instant overdraftTimestamp;
    private User owner;

    public CheckingAccount(User owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(int Id, User owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.accountId = Id;
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        BigDecimal availableBalance = this.balance.add(this.overdraftLimit);
        if (availableBalance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            if (this.balance.compareTo(BigDecimal.ZERO) < 0 && this.overdraftTimestamp == null) {
                this.overdraftTimestamp = Instant.now();
            }
        } else {
            throw new OverdraftLimitExceededException("Overdraft limit exceeded");
        }
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
        if (this.balance.compareTo(BigDecimal.ZERO) >= 0) {
            this.overdraftTimestamp = null;
        }
    }

    public BigDecimal applyOverdraftInterest(BigDecimal interestRate) {
        BigDecimal interest = this.balance.abs().multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
        this.balance = this.balance.subtract(interest);
        return interest;
    }
}
