package com.bankingsystem.persistence.dao;

import com.bankingsystem.models.CurrencyConversion;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;

public class CurrencyConversionDAO implements CurrencyConversionPersistenceService {

    private CurrencyConversion currentConversion;

    @Override
    public CurrencyConversion getLatestConversion() {
        return currentConversion;
    }

    @Override
    public void updateConversion(CurrencyConversion conversion) {
        this.currentConversion = conversion;
    }
}
