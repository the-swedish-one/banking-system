package com.bankingsystem.testutils;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.model.*;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class TestDataFactory {
    // Common defaults for reusable test data
    private static final int DEFAULT_ID = 1;
    private static final String DEFAULT_FIRST_NAME = "John";
    private static final String DEFAULT_LAST_NAME = "Doe";
    private static final String DEFAULT_EMAIL = "john.doe@example.com";
    private static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1000);
    private static final CurrencyCode DEFAULT_CURRENCY = CurrencyCode.EUR;
    private static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = BigDecimal.valueOf(500);
    private static final double DEFAULT_INTEREST_RATE = 2;

    private static final AtomicInteger idCounter = new AtomicInteger(1);

    // PersonDetails creation
    public static PersonDetails createPerson() {
        return createPerson(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME);
    }

    public static PersonDetails createPerson(String firstName, String lastName) {
        int uniqueId = idCounter.getAndIncrement();
        return new PersonDetails(uniqueId, firstName, lastName, DEFAULT_EMAIL, "123 Main St", "Apt 4B", "Springfield", "USA");
    }

    // User creation
    public static User createUser() {
        return createUser(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME);
    }

    public static User createUser(String firstName, String lastName) {
        return new User(createPerson(firstName, lastName));
    }

    // Transaction creation
    public static Transaction createTransaction() {
        return createTransaction(DEFAULT_AMOUNT, 1, 2);
    }

    public static Transaction createTransaction(BigDecimal amount, int fromAccountId, int toAccountId) {
        return new Transaction(amount, fromAccountId, toAccountId);
    }

    // CheckingAccount creation
    public static CheckingAccount createCheckingAccount(User user) {
        return createCheckingAccount(user, DEFAULT_AMOUNT, DEFAULT_CURRENCY, DEFAULT_OVERDRAFT_LIMIT);
    }

    public static CheckingAccount createCheckingAccount(User user, BigDecimal amount, CurrencyCode currency, BigDecimal overdraftLimit) {
        return new CheckingAccount(user, amount, currency, overdraftLimit);
    }

    // SavingsAccount creation
    public static SavingsAccount createSavingsAccount(User user) {
        return createSavingsAccount(user, DEFAULT_AMOUNT, DEFAULT_CURRENCY, DEFAULT_INTEREST_RATE);
    }

    public static SavingsAccount createSavingsAccount(User user, BigDecimal amount, CurrencyCode currency, double interestRate) {
        return new SavingsAccount(user, amount, currency, interestRate);
    }

    // JointCheckingAccount creation
    public static JointCheckingAccount createJointCheckingAccount(User primaryOwner, User secondaryOwner) {
        return createJointCheckingAccount(primaryOwner, secondaryOwner, DEFAULT_AMOUNT, DEFAULT_CURRENCY, DEFAULT_OVERDRAFT_LIMIT);
    }

    public static JointCheckingAccount createJointCheckingAccount(User primaryOwner, User secondaryOwner, BigDecimal amount, CurrencyCode currency, BigDecimal overdraftLimit) {
        return new JointCheckingAccount(primaryOwner, secondaryOwner, amount, currency, overdraftLimit);
    }
}
