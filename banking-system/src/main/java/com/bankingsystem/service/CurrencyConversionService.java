package com.bankingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bankingsystem.model.CurrencyCode;
import com.bankingsystem.model.CurrencyConversion;
import com.bankingsystem.persistence.dao.CurrencyConversionDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@Transactional
public class CurrencyConversionService {

    private final CurrencyConversionDAO currencyConversionDAO;

    @Autowired
    public CurrencyConversionService(CurrencyConversionDAO currencyConversionDAO) {
        this.currencyConversionDAO = currencyConversionDAO;
    }

    // Convert an amount from one currency to another
    public BigDecimal convertAmount(BigDecimal amount, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        CurrencyConversion conversion = currencyConversionDAO.getLatestConversion();
        BigDecimal rate = BigDecimal.valueOf(getExchangeRate(fromCurrency, toCurrency));
        BigDecimal convertedAmount = amount.multiply(rate);
        return convertedAmount.setScale(2, RoundingMode.HALF_UP);
    }

    // Get rate for exchanging one currency to another
    public double getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        CurrencyConversion conversion = currencyConversionDAO.getLatestConversion();
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
