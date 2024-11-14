package com.bankingsystem.persistence;

import com.bankingsystem.model.CurrencyConversionEntity;

public interface CurrencyConversionPersistenceService {
    CurrencyConversionEntity getLatestConversion();
    void updateConversion(CurrencyConversionEntity conversion);
}
