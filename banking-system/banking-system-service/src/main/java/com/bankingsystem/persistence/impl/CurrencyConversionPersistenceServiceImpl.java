package com.bankingsystem.persistence.impl;

import com.bankingsystem.model.CurrencyConversion;
import com.bankingsystem.model.CurrencyConversionEntity;
import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.persistence.CurrencyConversionPersistenceService;
import com.bankingsystem.repository.CurrencyConversionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConversionPersistenceServiceImpl implements CurrencyConversionPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionPersistenceServiceImpl.class);

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
                .orElseThrow(() -> {
                    logger.error("No currency conversion data found");
                    return new RuntimeException("No currency conversion data found");
                });
    }

    @Override
    public void updateConversion(CurrencyConversionEntity conversion) {
        logger.info("Updating currency conversion data");
        currencyConversionRepository.save(conversion);
        logger.info("Successfully updated currency conversion data");
    }
}
