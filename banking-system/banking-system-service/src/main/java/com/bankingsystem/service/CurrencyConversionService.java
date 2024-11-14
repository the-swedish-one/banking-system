package com.bankingsystem.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.mapper.CurrencyConversionMapper;
import com.bankingsystem.model.CurrencyConversion;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private final CurrencyConversionPersistenceService currencyConversionPersistenceService;
    private final CurrencyConversionMapper currencyConversionMapper;

    public CurrencyConversionService(CurrencyConversionPersistenceService currencyConversionPersistenceService,
                                     CurrencyConversionMapper currencyConversionMapper) {
        this.currencyConversionPersistenceService = currencyConversionPersistenceService;
        this.currencyConversionMapper = currencyConversionMapper;
    }

    // Convert an amount from one currency to another
    public BigDecimal convertAmount(BigDecimal amount, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        CurrencyConversion conversion = getLatestCurrencyConversion();
        BigDecimal rate = BigDecimal.valueOf(getExchangeRate(conversion, fromCurrency, toCurrency));
        BigDecimal convertedAmount = amount.multiply(rate);
        return convertedAmount.setScale(2, RoundingMode.HALF_UP);
    }

    // Get rate for exchanging one currency to another
    public double getExchangeRate(CurrencyConversion conversion, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
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

    // Retrieve the latest currency conversion data
    private CurrencyConversion getLatestCurrencyConversion() {
        var conversionEntity = currencyConversionPersistenceService.getLatestConversion();
        return currencyConversionMapper.toModel(conversionEntity);
    }

    // Update all exchange rates
    // Update all exchange rates
    public void updateExchangeRates(Map<CurrencyCode, Double> newRates) {
        CurrencyConversion conversion = new CurrencyConversion(newRates);
        conversion.setLastUpdated(new Date());
        var conversionEntity = currencyConversionMapper.toEntity(conversion);
        currencyConversionPersistenceService.updateConversion(conversionEntity);
    }
}
