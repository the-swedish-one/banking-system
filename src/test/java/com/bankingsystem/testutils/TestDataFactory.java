package com.bankingsystem.testutils;

import com.bankingsystem.models.*;


public class TestDataFactory {
    public static Bank createBank(String name, String bic) {
        return new Bank(name, bic);
    }

    public static Person createPerson(String firstName, String lastName, String email) {
        return new Person(firstName, lastName, email, "Address Line 1", "Address Line 2", "City", "Country");
    }

    public static User createUser(String firstName, String lastName, String email) {
        Person person = createPerson(firstName, lastName, email);
        return new User(person);
    }

    public static DepositTransaction createDepositTransaction(double amount) {
        return new DepositTransaction(amount);
    }

    public static WithdrawTransaction createWithdrawTransaction(double amount) {
        return new WithdrawTransaction(amount);
    }

    public static TransferTransaction createTransferTransaction(double amount, String fromAccountId, String toAccountId) {
        return new TransferTransaction(amount, fromAccountId, toAccountId);
    }

    public static CheckingAccount createCheckingAccount(User user, double amount, CurrencyCode currency, double overdraftLimit) {
        return new CheckingAccount(user, amount, currency, overdraftLimit);
    }

    public static SavingsAccount createSavingsAccount(User user, double amount, CurrencyCode currency, double interestRate) {
        return new SavingsAccount(user, amount, currency, interestRate);
    }

    public static JointCheckingAccount createJointCheckingAccount(User user, User secondOwner, double amount, CurrencyCode currency, double overdraftLimit) {
        return new JointCheckingAccount(user, secondOwner, amount, currency, overdraftLimit);
    }

}
