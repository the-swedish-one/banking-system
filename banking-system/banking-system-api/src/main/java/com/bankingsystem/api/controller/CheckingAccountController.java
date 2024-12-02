package com.bankingsystem.api.controller;

import com.bankingsystem.domain.model.CheckingAccount;
import com.bankingsystem.domain.service.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/checking-account")
public class CheckingAccountController {

    private final CheckingAccountService checkingAccountService;

    @Autowired
    public CheckingAccountController(CheckingAccountService checkingAccountService) {
        this.checkingAccountService = checkingAccountService;
    }

    // Create new checking account
    @PostMapping
    public ResponseEntity<CheckingAccount> createCheckingAccount(@RequestBody CheckingAccount checkingAccount) {
        CheckingAccount createdAccount = checkingAccountService.createCheckingAccount(checkingAccount);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Get checking account by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<CheckingAccount> getCheckingAccountById(@PathVariable int accountId) {
        CheckingAccount account = checkingAccountService.getCheckingAccountById(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Get all checking accounts
    @GetMapping
    public ResponseEntity<List<CheckingAccount>> getAllCheckingAccounts() {
        List<CheckingAccount> accounts = checkingAccountService.getAllCheckingAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Update checking account
    @PutMapping("/{accountId}")
    public ResponseEntity<CheckingAccount> updateCheckingAccount(@PathVariable int accountId, @RequestBody CheckingAccount checkingAccount) {
        checkingAccount.setAccountId(accountId);
        CheckingAccount updatedAccount = checkingAccountService.updateCheckingAccount(checkingAccount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Delete checking account by ID
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteCheckingAccount(@PathVariable int accountId) {
        boolean isDeleted = checkingAccountService.deleteCheckingAccount(accountId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Deposit money into checking account
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<CheckingAccount> deposit(@PathVariable int accountId, @RequestBody BigDecimal amount) {
            CheckingAccount updatedAccount = checkingAccountService.deposit(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Withdraw money from checking account
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<CheckingAccount> withdraw(@PathVariable int accountId, @RequestBody BigDecimal amount) {
            CheckingAccount updatedAccount = checkingAccountService.withdraw(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Transfer between two checking accounts
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestParam int fromAccountId, @RequestParam int toAccountId, @RequestParam BigDecimal amount) {
            checkingAccountService.transfer(amount, fromAccountId, toAccountId);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
