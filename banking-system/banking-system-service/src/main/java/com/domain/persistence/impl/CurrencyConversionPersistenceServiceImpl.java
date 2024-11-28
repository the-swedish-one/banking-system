package com.domain.persistence.impl;

import com.domain.mapper.CurrencyConversionMapper;
import com.domain.model.CurrencyConversion;
import com.bankingsystem.model.CurrencyConversionEntity;
import com.bankingsystem.enums.CurrencyCode;
import com.domain.persistence.CurrencyConversionPersistenceService;
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
    private final CurrencyConversionMapper currencyConversionMapper;

    @Autowired
    public CurrencyConversionPersistenceServiceImpl(CurrencyConversionRepository currencyConversionRepository, CurrencyConversionMapper currencyConversionMapper) {

        this.currencyConversionRepository = currencyConversionRepository;
        this.currencyConversionMapper = currencyConversionMapper;

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
    public CurrencyConversion getLatestConversion() {
        return currencyConversionRepository.findTopByOrderByTimestampDesc()
                .map(currencyConversionMapper::toModel)
                .orElseThrow(() -> {
                    logger.error("No currency conversion data found");
                    return new RuntimeException("No currency conversion data found");
                });
    }

    @Override
    public void updateConversion(CurrencyConversion conversion) {
        logger.info("Updating currency conversion data");
        CurrencyConversionEntity entity = currencyConversionMapper.toEntity(conversion);
        currencyConversionRepository.save(entity);
        logger.info("Successfully updated currency conversion data");
    }
}
