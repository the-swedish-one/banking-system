package com.bankingsystem.models;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String bic;
    private List<User> users;
    private List<Account> accounts;

    public Bank(String bic) {
        this.bic = bic;
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
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

}
