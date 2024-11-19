package com.bankingsystem.persistence;

import com.bankingsystem.model.Bank;

public interface BankPersistenceService {

    Bank save(Bank bank);

    Bank getBankByBic(String bic);

    boolean deleteBank(String bic);
}
