package com.bankingsystem.api.controller;

import com.bankingsystem.domain.model.SavingsAccount;
import com.bankingsystem.domain.service.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/savings-account")
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @Autowired
    public SavingsAccountController(SavingsAccountService savingsAccountService) {
        this.savingsAccountService = savingsAccountService;
    }

    // Create new savings account
    @PostMapping
    public ResponseEntity<SavingsAccount> createSavingsAccount(@RequestBody SavingsAccount account) {
        SavingsAccount createdAccount = savingsAccountService.createSavingsAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Get savings account by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<SavingsAccount> getSavingsAccountById(@PathVariable int accountId) {
            SavingsAccount account = savingsAccountService.getSavingsAccountById(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Get all savings accounts
    @GetMapping
    public ResponseEntity<List<SavingsAccount>> getAllSavingsAccounts() {
        List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Update savings account
    @PutMapping("/{accountId}")
    public ResponseEntity<SavingsAccount> updateSavingsAccount(@PathVariable int accountId, @RequestBody SavingsAccount account) {
            account.setAccountId(accountId);  // Ensure the correct account ID is set
            SavingsAccount updatedAccount = savingsAccountService.updateSavingsAccount(account);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Delete savings account by ID
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteSavingsAccount(@PathVariable int accountId) {
            boolean deleted = savingsAccountService.deleteSavingsAccount(accountId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    // Deposit into savings account
    @PostMapping("/deposit")
    public ResponseEntity<SavingsAccount> deposit(@RequestParam int accountId, @RequestParam BigDecimal amount) {
            SavingsAccount updatedAccount = savingsAccountService.deposit(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Withdraw from savings account
    @PostMapping("/withdraw")
    public ResponseEntity<SavingsAccount> withdraw(@RequestParam int accountId, @RequestParam BigDecimal amount) {
            SavingsAccount updatedAccount = savingsAccountService.withdraw(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Transfer money between savings accounts
    @PostMapping("/transfer")
    public ResponseEntity<List<SavingsAccount>> transfer(
            @RequestParam int fromAccountId,
            @RequestParam int toAccountId,
            @RequestParam BigDecimal amount) {
        List<SavingsAccount> updatedAccounts = savingsAccountService.transfer(amount, fromAccountId, toAccountId);
            return new ResponseEntity<>(updatedAccounts, HttpStatus.OK);
    }

    // Apply interest to a savings account
    @PostMapping("/interest")
    public ResponseEntity<SavingsAccount> applyInterest(@RequestParam int accountId) {
            SavingsAccount account = savingsAccountService.getSavingsAccountById(accountId);
            SavingsAccount updatedAccount = savingsAccountService.addInterest(account);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }
}
