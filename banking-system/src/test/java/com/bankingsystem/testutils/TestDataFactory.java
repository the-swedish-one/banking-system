package com.bankingsystem.testutils;

import com.bankingsystem.model.*;

import java.math.BigDecimal;


public class TestDataFactory {
    public static Bank createBank(String name, String bic) {
        return new Bank(name, bic);
    }

    public static PersonDetails createPerson(String firstName, String lastName, String email) {
        return new PersonDetails(firstName, lastName, email, "Address Line 1", "Address Line 2", "City", "Country");
    }

    public static User createUser(String firstName, String lastName, String email) {
        PersonDetails person = createPerson(firstName, lastName, email);
        return new User(person);
    }

    public static DepositTransaction createDepositTransaction(BigDecimal amount) {
        return new DepositTransaction(amount);
    }

    public static WithdrawTransaction createWithdrawTransaction(BigDecimal amount) {
        return new WithdrawTransaction(amount);
    }

    public static TransferTransaction createTransferTransaction(BigDecimal amount, String fromAccountId, String toAccountId) {
        return new TransferTransaction(amount, fromAccountId, toAccountId);
    }

    public static CheckingAccount createCheckingAccount(User user, BigDecimal amount, CurrencyCode currency, BigDecimal overdraftLimit) {
        return new CheckingAccount(user, amount, currency, overdraftLimit);
    }

    public static SavingsAccount createSavingsAccount(User user, BigDecimal amount, CurrencyCode currency, double interestRate) {
        return new SavingsAccount(user, amount, currency, interestRate);
    }

    public static JointCheckingAccount createJointCheckingAccount(User user, User secondOwner, BigDecimal amount, CurrencyCode currency, BigDecimal overdraftLimit) {
        return new JointCheckingAccount(user, secondOwner, amount, currency, overdraftLimit);
    }

}
