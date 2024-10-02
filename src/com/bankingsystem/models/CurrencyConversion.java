package com.bankingsystem.models;

import java.util.Date;
import java.util.Map;

public class CurrencyConversion {
    private Date lastUpdated;
    private Map<CurrencyCode, Double> exchangeRates;

    public CurrencyConversion(Map<CurrencyCode, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.lastUpdated = new Date();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<CurrencyCode, Double> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<CurrencyCode, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}
