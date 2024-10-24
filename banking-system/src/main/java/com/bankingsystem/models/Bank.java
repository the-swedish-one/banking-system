package com.bankingsystem.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bank {
    private String bankName;
    private String bankId;
    private List<User> users;
    private List<Account> accounts;

    public Bank(String bankName, String bankId) {
        this.bankName = bankName;
        this.bankId = bankId;
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankId() {
        return this.bankId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bic=" + bankId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;
        // Null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;

        Bank bank = (Bank) obj;
        // Compare fields for logical equality
        return Objects.equals(bankId, bank.bankId) &&
                Objects.equals(bankName, bank.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankId, bankName);
    }

}
