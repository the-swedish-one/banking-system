package com.bankingsystem.domain.persistence;

import com.bankingsystem.domain.model.CurrencyConversion;

public interface CurrencyConversionPersistenceService {
    CurrencyConversion getLatestConversion();
    void updateConversion(CurrencyConversion conversion);
}
