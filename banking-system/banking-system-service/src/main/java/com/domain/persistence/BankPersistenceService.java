package com.domain.persistence;

import com.domain.model.Bank;

public interface BankPersistenceService {

    Bank save(Bank bank);

    Bank getBankByBic(String bic);

}
