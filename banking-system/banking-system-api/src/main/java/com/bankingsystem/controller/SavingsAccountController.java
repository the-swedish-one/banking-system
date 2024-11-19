package com.bankingsystem.controller;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InsufficientFundsException;
import com.bankingsystem.model.SavingsAccount;
import com.bankingsystem.service.SavingsAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/savings-accounts")
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    public SavingsAccountController(SavingsAccountService savingsAccountService) {
        this.savingsAccountService = savingsAccountService;
    }

    // Create new savings account
    @PostMapping
    public ResponseEntity<SavingsAccount> createSavingsAccount(@RequestBody SavingsAccount account) {
        try {
            SavingsAccount createdAccount = savingsAccountService.createSavingsAccount(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get savings account by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<SavingsAccount> getSavingsAccountById(@PathVariable int accountId) {
        try {
            SavingsAccount account = savingsAccountService.getSavingsAccountById(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (IllegalArgumentException | AccountNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get all savings accounts
    @GetMapping
    public ResponseEntity<List<SavingsAccount>> getAllSavingsAccounts() {
        try {
            List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Update savings account
    @PutMapping("/{accountId}")
    public ResponseEntity<SavingsAccount> updateSavingsAccount(@PathVariable int accountId, @RequestBody SavingsAccount account) {
        try {
            account.setAccountId(accountId);  // Ensure the correct account ID is set
            SavingsAccount updatedAccount = savingsAccountService.updateSavingsAccount(account);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete savings account by ID
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteSavingsAccount(@PathVariable int accountId) {
        try {
            boolean deleted = savingsAccountService.deleteSavingsAccount(accountId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Deposit into savings account
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<SavingsAccount> deposit(@PathVariable int accountId, @RequestParam BigDecimal amount) {
        try {
            SavingsAccount updatedAccount = savingsAccountService.deposit(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Withdraw from savings account
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<SavingsAccount> withdraw(@PathVariable int accountId, @RequestParam BigDecimal amount) {
        try {
            SavingsAccount updatedAccount = savingsAccountService.withdraw(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException | InsufficientFundsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Transfer money between savings accounts
    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Void> transfer(
            @PathVariable int fromAccountId,
            @PathVariable int toAccountId,
            @RequestParam BigDecimal amount) {
        try {
            savingsAccountService.transfer(amount, fromAccountId, toAccountId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException | InsufficientFundsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Apply interest to a savings account
    @PostMapping("/{accountId}/interest")
    public ResponseEntity<SavingsAccount> applyInterest(@PathVariable int accountId) {
        try {
            SavingsAccount account = savingsAccountService.getSavingsAccountById(accountId);
            SavingsAccount updatedAccount = savingsAccountService.addInterest(account);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
