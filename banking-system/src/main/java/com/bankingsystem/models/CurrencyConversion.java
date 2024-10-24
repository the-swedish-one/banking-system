package com.bankingsystem.models;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "CurrencyConversion{" +
                "lastUpdated=" + lastUpdated +
                ", exchangeRates=" + exchangeRates +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;
        // Null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;

        CurrencyConversion currencyConversion = (CurrencyConversion) obj;
        // Compare fields for logical equality
        return Objects.equals(exchangeRates, currencyConversion.exchangeRates) &&
                Objects.equals(lastUpdated, currencyConversion.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangeRates, lastUpdated);
    }
}
