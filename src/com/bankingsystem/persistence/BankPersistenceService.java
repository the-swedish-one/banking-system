package com.bankingsystem.persistence;

import com.bankingsystem.models.Bank;

public interface BankPersistenceService {

    void createBank(Bank bank);

    Bank getBankByBic(String bic);

    void updateBank(Bank bank);

    boolean deleteBank(String bic);
}
