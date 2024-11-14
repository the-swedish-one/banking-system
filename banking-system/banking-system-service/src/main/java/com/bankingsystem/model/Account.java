package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account implements Withdrawable, Depositable {

    protected int accountId;
    protected String iban;
    protected String accountName;
    protected User owner;
    protected BigDecimal balance;
    protected CurrencyCode currency;

    public Account(User owner, BigDecimal balance, CurrencyCode currency) {
        this.iban = generateIBAN(owner.getPerson().getCountry()); // TODO - call account service to generate IBAN
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
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

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit failed: Amount must be greater than 0");
        }
        this.balance = this.balance.add(amount);
    }
}
