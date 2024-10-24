package com.bankingsystem.persistence;

import com.bankingsystem.models.Bank;

public interface BankPersistenceService {

    Bank save(Bank bank);

    Bank getBankByBic(String bic);

    void updateBank(Bank bank);

    boolean deleteBank(String bic);
}
