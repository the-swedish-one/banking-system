package com.bankingsystem.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bank {
    private String bankName;
    private String bic;
    private List<User> users;
    private List<Account> accounts;

    public Bank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBic() {
        return this.bic;
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
                "bic=" + bic + '\'' +
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
        return Objects.equals(bic, bank.bic) &&
                Objects.equals(bankName, bank.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bic, bankName);
    }

}
