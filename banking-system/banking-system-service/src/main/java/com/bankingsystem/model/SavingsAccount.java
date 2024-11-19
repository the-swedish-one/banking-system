package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.InsufficientFundsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccount implements Withdrawable, Depositable  {

    private int accountId;
    private String iban;
    private String accountName;
    private User owner;
    private BigDecimal balance;
    private CurrencyCode currency;
    private double interestRatePercentage;

    public SavingsAccount(User owner, BigDecimal balance, CurrencyCode currency, double interestRate) {
        this.iban = generateIBAN(owner.getPerson().getCountry()); // TODO - call account service to generate IBAN
        this.owner = owner;
        this.balance = balance;
        this.interestRatePercentage = interestRate;
    }

    // TODO - move to Account Service
    private String generateIBAN(String country) {
        String countryCode = country.substring(0, 2).toUpperCase();
        Random random = new Random();

        String randomDigits = IntStream.range(0, 14)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());

        return countryCode + randomDigits;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw Failed: Amount must be greater than 0");
        }
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
        } else {
            throw new InsufficientFundsException("Withdraw Failed: Insufficient funds");
        }
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit failed: Amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
    }
}

