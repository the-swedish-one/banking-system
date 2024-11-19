package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.model.JointCheckingAccount;
import com.bankingsystem.persistence.JointCheckingAccountPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JointCheckingAccountService {

    private final JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService;

    public JointCheckingAccountService(JointCheckingAccountPersistenceService jointCheckingAccountPersistenceService) {
        this.jointCheckingAccountPersistenceService = jointCheckingAccountPersistenceService;
    }

    // Create new joint checking account
    public JointCheckingAccount createJointCheckingAccount(JointCheckingAccount jointCheckingAccount) {
        return jointCheckingAccountPersistenceService.save(jointCheckingAccount);
    }

    // Get joint checking account by ID
    public JointCheckingAccount getJointCheckingAccountById(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return jointCheckingAccountPersistenceService.getAccountById(accountId);
    }

    // Get all joint checking accounts
    public List<JointCheckingAccount> getAllJointCheckingAccounts() {
        List<JointCheckingAccount> jointCheckingAccountList = jointCheckingAccountPersistenceService.getAllAccounts();
        if (jointCheckingAccountList.isEmpty()) {
            throw new AccountNotFoundException("No joint checking accounts found");
        }
        return jointCheckingAccountList;
    }

    // Update joint checking account
    public JointCheckingAccount updateJointCheckingAccount(JointCheckingAccount jointCheckingAccount) {
        return jointCheckingAccountPersistenceService.updateAccount(jointCheckingAccount);
    }

    // Delete joint checking account by ID
    public boolean deleteJointCheckingAccount(int accountId) {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be greater than zero");
        }
        return jointCheckingAccountPersistenceService.deleteAccount(accountId);
    }
}
