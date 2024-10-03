package com.bankingsystem.services;

import com.bankingsystem.models.CurrencyCode;
import com.bankingsystem.models.CurrencyConversion;
import com.bankingsystem.persistence.dao.CurrencyConversionDAO;

import java.util.Map;

public class CurrencyConversionService {

    private final CurrencyConversionDAO currencyConversionDAO;

    public CurrencyConversionService(CurrencyConversionDAO currencyConversionDAO) {
        this.currencyConversionDAO = currencyConversionDAO;
    }

    // Convert an amount from one currency to another
    public double convertAmount(double amount, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        CurrencyConversion conversion = currencyConversionDAO.getLatestConversion();
        double rate = getExchangeRate(conversion, fromCurrency, toCurrency);
        return amount * rate;
    }

    // Get rate for exchanging one currency to another
    private double getExchangeRate(CurrencyConversion conversion, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        if (fromCurrency == toCurrency) {
            return 1.0;
        }
        Double fromRate = conversion.getExchangeRates().get(fromCurrency);
        Double toRate = conversion.getExchangeRates().get(toCurrency);

        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Unsupported currency conversion");
        }

        return toRate / fromRate;
    }

    // Update all exchange rates
    public void updateExchangeRates(Map<CurrencyCode, Double> newRates) {
        CurrencyConversion conversion = new CurrencyConversion(newRates);
        conversion.setLastUpdated(new java.util.Date());
        currencyConversionDAO.updateConversion(conversion);
    }
}
