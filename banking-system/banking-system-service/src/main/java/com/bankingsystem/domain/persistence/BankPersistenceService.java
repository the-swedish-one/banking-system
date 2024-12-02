package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.Bank;

public interface BankPersistenceService {

    Bank save(Bank bank);

    Bank getBankByBic(String bic);

}
