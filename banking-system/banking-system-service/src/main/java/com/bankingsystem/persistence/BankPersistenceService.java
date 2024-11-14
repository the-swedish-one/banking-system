package com.bankingsystem.persistence;

import com.bankingsystem.model.Bank;
import com.bankingsystem.model.BankEntity;

public interface BankPersistenceService {

    Bank save(Bank bank);

    Bank getBankByBic(String bic);

    boolean deleteBank(String bic);
}
