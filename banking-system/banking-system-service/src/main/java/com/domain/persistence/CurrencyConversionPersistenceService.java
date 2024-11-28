package com.domain.persistence;

import com.domain.model.CurrencyConversion;

public interface CurrencyConversionPersistenceService {
    CurrencyConversion getLatestConversion();
    void updateConversion(CurrencyConversion conversion);
}
