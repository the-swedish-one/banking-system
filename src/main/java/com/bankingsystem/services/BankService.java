package com.bankingsystem.services;

import com.bankingsystem.models.Account;
import com.bankingsystem.models.Bank;
import com.bankingsystem.models.User;
import com.bankingsystem.models.exceptions.AccountNotFoundException;
import com.bankingsystem.models.exceptions.BankNotFoundException;
import com.bankingsystem.models.exceptions.UserNotFoundException;
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
        if (bank == null) {
            throw new BankNotFoundException("Bank not found");
        }
        if (bank.getUsers().isEmpty()) {
            throw new UserNotFoundException("No users found");
        }

        StringBuilder users = new StringBuilder();
        users.append("Users of bank ").append(bank.getBankName()).append(":\n");
        for (User user : bank.getUsers()) {
            users.append(user.getPerson().getFirstName()).append(" ")
                    .append(user.getPerson().getLastName()).append("\n");
        }

        return users.toString();
    }


    // Get all accounts of the bank
    public String getAllBankAccounts(String bic) {
        Bank bank = bankPersistenceService.getBankByBic(bic);
        if (bank == null) {
            throw new BankNotFoundException("Bank not found");
        }
        if (bank.getAccounts().isEmpty()) {
            throw new AccountNotFoundException("No accounts found");
        }
        StringBuilder accounts = new StringBuilder();
        accounts.append("Accounts of bank ").append(bank.getBankName()).append(":\n");
        for (Account account : bank.getAccounts()) {
            accounts.append(account.toString()).append("\n");
        }
        return accounts.toString();
    }
}
