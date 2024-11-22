package com.bankingsystem.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.mapper.CurrencyConversionMapper;
import com.bankingsystem.model.CurrencyConversion;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

    private final CurrencyConversionPersistenceService currencyConversionPersistenceService;
    private final CurrencyConversionMapper currencyConversionMapper;

    public CurrencyConversionService(CurrencyConversionPersistenceService currencyConversionPersistenceService,
                                     CurrencyConversionMapper currencyConversionMapper) {
        this.currencyConversionPersistenceService = currencyConversionPersistenceService;
        this.currencyConversionMapper = currencyConversionMapper;
    }

    // Convert an amount from one currency to another
    public BigDecimal convertAmount(BigDecimal amount, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        logger.info("Converting {} {} to {}", amount, fromCurrency, toCurrency);
        CurrencyConversion conversion = getLatestCurrencyConversion();
        BigDecimal rate = BigDecimal.valueOf(getExchangeRate(conversion, fromCurrency, toCurrency));
        BigDecimal convertedAmount = amount.multiply(rate);
        logger.info("Converted amount: {} {}", convertedAmount.setScale(2, RoundingMode.HALF_UP), toCurrency);
        return convertedAmount.setScale(2, RoundingMode.HALF_UP);
    }

    // Get rate for exchanging one currency to another
    public double getExchangeRate(CurrencyConversion conversion, CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        logger.info("Getting exchange rate from {} to {}", fromCurrency, toCurrency);
        if (fromCurrency == toCurrency) {
            logger.info("Same currency, no conversion needed");
            return 1.0;
        }
        Double fromRate = conversion.getExchangeRates().get(fromCurrency);
        Double toRate = conversion.getExchangeRates().get(toCurrency);

        if (fromRate == null || toRate == null) {
            logger.error("Unsupported currency conversion");
            throw new IllegalArgumentException("Unsupported currency conversion");
        }
        logger.info("Exchange rate: {}", toRate / fromRate);
        return toRate / fromRate;
    }

    // Retrieve the latest currency conversion data
    public CurrencyConversion getLatestCurrencyConversion() {
        logger.info("Fetching latest currency conversion data");
        var conversionEntity = currencyConversionPersistenceService.getLatestConversion();
        logger.info("Successfully fetched latest currency conversion data");
        return currencyConversionMapper.toModel(conversionEntity);
    }

    // Update all exchange rates
    public void updateExchangeRates(Map<CurrencyCode, Double> newRates) {
        logger.info("Updating exchange rates");
        CurrencyConversion conversion = new CurrencyConversion(newRates);
        conversion.setLastUpdated(new Date());
        var conversionEntity = currencyConversionMapper.toEntity(conversion);
        currencyConversionPersistenceService.updateConversion(conversionEntity);
    }
}
