package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.Bank;

import java.util.ArrayList;
import java.util.List;

public class BankDAO {

    private List<Bank> banks = new ArrayList<Bank>();

    // Create new bank
    public void createBank(Bank bank){
        banks.add(bank);
    }

}
