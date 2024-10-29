package com.bankingsystem.persistence.dao;

import com.bankingsystem.model.CurrencyCode;
import com.bankingsystem.model.CurrencyConversion;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConversionDAO implements CurrencyConversionPersistenceService {

    private CurrencyConversion exchangeRates;

    public CurrencyConversionDAO() {
        Map<CurrencyCode, Double> initialRates = new HashMap<>();
        initialRates.put(CurrencyCode.EUR, 1.0); // Base currency
        initialRates.put(CurrencyCode.USD, 1.1);
        initialRates.put(CurrencyCode.GBP, 0.9);
        initialRates.put(CurrencyCode.SEK, 10.0);
        initialRates.put(CurrencyCode.CHF, 1.2);

        exchangeRates = new CurrencyConversion(initialRates);
    }

    @Override
    public CurrencyConversion getLatestConversion() {
        return exchangeRates;
    }

    @Override
    public void updateConversion(CurrencyConversion conversion) {
        this.exchangeRates = conversion;
    }
}
