package com.bankingsystem.persistence;

import com.bankingsystem.model.BankEntity;

public interface BankPersistenceService {

    BankEntity save(BankEntity bank);

    BankEntity getBankByBic(String bic);

    void updateBank(BankEntity bank);

    boolean deleteBank(String bic);
}
