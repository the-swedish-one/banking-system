package com.bankingsystem.persistence.impl;

import com.bankingsystem.model.CurrencyConversionEntity;
import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;
import com.bankingsystem.repository.CurrencyConversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConversionPersistenceServiceImpl implements CurrencyConversionPersistenceService {

    private final CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    public CurrencyConversionPersistenceServiceImpl(CurrencyConversionRepository currencyConversionRepository) {
        this.currencyConversionRepository = currencyConversionRepository;

        // Initialize if no conversion data exists in the repository
        if (currencyConversionRepository.count() == 0) {
            Map<CurrencyCode, Double> initialRates = new HashMap<>();
            initialRates.put(CurrencyCode.EUR, 1.0); // Base currency
            initialRates.put(CurrencyCode.USD, 1.1);
            initialRates.put(CurrencyCode.GBP, 0.9);
            initialRates.put(CurrencyCode.SEK, 10.0);
            initialRates.put(CurrencyCode.CHF, 1.2);

            CurrencyConversionEntity initialConversion = new CurrencyConversionEntity(initialRates);
            currencyConversionRepository.save(initialConversion);
        }
    }

    @Override
    public CurrencyConversionEntity getLatestConversion() {
        return currencyConversionRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new RuntimeException("No currency conversion data found"));
    }

    @Override
    public void updateConversion(CurrencyConversionEntity conversion) {
        currencyConversionRepository.save(conversion);
    }
}
