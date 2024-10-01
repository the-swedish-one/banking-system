package com.bankingsystem.services;

import com.bankingsystem.models.Bank;
import com.bankingsystem.persistence.BankDAO;

public class BankService {

    private BankDAO bankDAO = new BankDAO();

    public void createBank(String bic) {
        Bank bank = new Bank(bic);
        bankDAO.createBank(bank);
    }
}
