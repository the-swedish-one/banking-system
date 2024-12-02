package com.bankingsystem.api.controller;

import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.domain.service.JointCheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/joint-checking-account")
public class JointCheckingAccountController {

    private final JointCheckingAccountService jointCheckingAccountService;

    @Autowired
    public JointCheckingAccountController(JointCheckingAccountService jointCheckingAccountService) {
        this.jointCheckingAccountService = jointCheckingAccountService;
    }

    @PostMapping
    public ResponseEntity<JointCheckingAccount> createJointCheckingAccount(@RequestBody JointCheckingAccount jointCheckingAccount) {
            JointCheckingAccount createdAccount = jointCheckingAccountService.createJointCheckingAccount(jointCheckingAccount);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<JointCheckingAccount> getJointCheckingAccountById(@PathVariable int accountId) {
            JointCheckingAccount account = jointCheckingAccountService.getJointCheckingAccountById(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<JointCheckingAccount>> getAllJointCheckingAccounts() {
            List<JointCheckingAccount> accounts = jointCheckingAccountService.getAllJointCheckingAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<JointCheckingAccount> updateJointCheckingAccount(@PathVariable int accountId, @RequestBody JointCheckingAccount jointCheckingAccount) {
        jointCheckingAccount.setAccountId(accountId);
            JointCheckingAccount updatedAccount = jointCheckingAccountService.updateJointCheckingAccount(jointCheckingAccount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteJointCheckingAccount(@PathVariable int accountId) {
            boolean isDeleted = jointCheckingAccountService.deleteJointCheckingAccount(accountId);
            return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<JointCheckingAccount> deposit(@PathVariable int accountId, @RequestParam BigDecimal amount) {
            JointCheckingAccount updatedAccount = jointCheckingAccountService.deposit(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<JointCheckingAccount> withdraw(@PathVariable int accountId, @RequestParam BigDecimal amount) {
            JointCheckingAccount updatedAccount = jointCheckingAccountService.withdraw(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Void> transfer(@PathVariable int fromAccountId, @PathVariable int toAccountId, @RequestParam BigDecimal amount) {
            jointCheckingAccountService.transfer(amount, fromAccountId, toAccountId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
