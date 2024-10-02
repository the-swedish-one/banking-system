package com.bankingsystem.models;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class CurrencyConversion {
    private Date lastUpdated;
    private Map<CurrencyCode, Double> exchangeRates;

    public CurrencyConversion(Map<CurrencyCode, Double> initialRates) {
        this.exchangeRates = initialRates;
        this.lastUpdated = new Date();
    }

    public double getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        if (fromCurrency == toCurrency) {
            return 1.0;
        }
        Double fromRate = exchangeRates.get(fromCurrency);
        Double toRate = exchangeRates.get(toCurrency);
        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Unsupported currency conversion");
        }
        return toRate / fromRate;
    }

    public double convertAmount(double amount, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        double rate = getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }

    public void updateExchangeRates(Map<CurrencyCode, Double> newRates) {
        this.exchangeRates = newRates;
        this.lastUpdated = new Date();
    }



    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
