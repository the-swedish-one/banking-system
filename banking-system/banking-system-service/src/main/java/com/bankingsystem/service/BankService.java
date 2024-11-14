package com.bankingsystem.service;

import org.springframework.stereotype.Service;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Bank;
import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.BankNotFoundException;
import com.bankingsystem.exception.UserNotFoundException;
import com.bankingsystem.persistence.BankPersistenceService;

import java.util.stream.Collectors;

@Service
public class BankService {

    private final BankPersistenceService bankPersistenceService;

    public BankService(BankPersistenceService bankPersistenceService) {
        this.bankPersistenceService = bankPersistenceService;
    }

    public Bank createBank(String bankName, String bic) {
        Bank bank = new Bank(bankName, bic);
        bankPersistenceService.save(bank);
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

        return "Users of bank " + bank.getBankName() + ":\n" +
                bank.getUsers().stream()
                        .map(user -> user.getPerson().getFirstName() + " " + user.getPerson().getLastName())
                        .collect(Collectors.joining("\n"));
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

        return "Accounts of bank " + bank.getBankName() + ":\n" +
                bank.getAccounts().stream()
                        .map(Account::toString)
                        .collect(Collectors.joining("\n"));
    }
}
