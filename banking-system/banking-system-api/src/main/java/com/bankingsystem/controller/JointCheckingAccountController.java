package com.bankingsystem.controller;

import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.model.JointCheckingAccount;
import com.bankingsystem.service.JointCheckingAccountService;
import com.bankingsystem.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/joint-checking-account")
public class JointCheckingAccountController {

    private final JointCheckingAccountService jointCheckingAccountService;

    public JointCheckingAccountController(JointCheckingAccountService jointCheckingAccountService) {
        this.jointCheckingAccountService = jointCheckingAccountService;
    }

    @PostMapping
    public ResponseEntity<JointCheckingAccount> createJointCheckingAccount(@RequestBody JointCheckingAccount jointCheckingAccount) {
        try {
            JointCheckingAccount createdAccount = jointCheckingAccountService.createJointCheckingAccount(jointCheckingAccount);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<JointCheckingAccount> getJointCheckingAccountById(@PathVariable int accountId) {
        try {
            JointCheckingAccount account = jointCheckingAccountService.getJointCheckingAccountById(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<JointCheckingAccount>> getAllJointCheckingAccounts() {
        try {
            List<JointCheckingAccount> accounts = jointCheckingAccountService.getAllJointCheckingAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<JointCheckingAccount> updateJointCheckingAccount(@PathVariable int accountId, @RequestBody JointCheckingAccount jointCheckingAccount) {
        jointCheckingAccount.setAccountId(accountId);
        try {
            JointCheckingAccount updatedAccount = jointCheckingAccountService.updateJointCheckingAccount(jointCheckingAccount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteJointCheckingAccount(@PathVariable int accountId) {
        try {
            boolean isDeleted = jointCheckingAccountService.deleteJointCheckingAccount(accountId);
            return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<JointCheckingAccount> deposit(@PathVariable int accountId, @RequestParam BigDecimal amount) {
        try {
            JointCheckingAccount updatedAccount = jointCheckingAccountService.deposit(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<JointCheckingAccount> withdraw(@PathVariable int accountId, @RequestParam BigDecimal amount) {
        try {
            JointCheckingAccount updatedAccount = jointCheckingAccountService.withdraw(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (OverdraftLimitExceededException | IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Void> transfer(@PathVariable int fromAccountId, @PathVariable int toAccountId, @RequestParam BigDecimal amount) {
        try {
            jointCheckingAccountService.transfer(amount, fromAccountId, toAccountId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | OverdraftLimitExceededException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
