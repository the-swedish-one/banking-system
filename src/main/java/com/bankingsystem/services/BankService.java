package com.bankingsystem.services;

import com.bankingsystem.models.Account;
import com.bankingsystem.models.Bank;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.BankPersistenceService;

public class BankService {

    private final BankPersistenceService bankPersistenceService;

    public BankService(BankPersistenceService bankPersistenceService) {
        this.bankPersistenceService = bankPersistenceService;
    }

    public Bank createBank(String bankName, String bic) {
        Bank bank = new Bank(bankName, bic);
        bankPersistenceService.createBank(bank);
        return bank;
    }

    // Get all users of the bank
    public String getAllBankUsers(String bic) {
        Bank bank = bankPersistenceService.getBankByBic(bic);
        StringBuilder users = new StringBuilder();
        users.append("Users of bank ").append(bank.getBankName()).append(":\n");
        for (User user : bank.getUsers()) {
            users.append(user.toString()).append("\n");
        }
        if (users.isEmpty()) {
            return "No users found";
        }
        return users.toString();
    }

    // Get all accounts of the bank
    public String getAllBankAccounts(String bic) {
        Bank bank = bankPersistenceService.getBankByBic(bic);
        StringBuilder accounts = new StringBuilder();
        accounts.append("Accounts of bank ").append(bank.getBankName()).append(":\n");
        for (Account account : bank.getAccounts()) {
            accounts.append(account.toString()).append("\n");
        }
        if (accounts.isEmpty()) {
            return "No accounts found";
        }
        return accounts.toString();
    }
}
