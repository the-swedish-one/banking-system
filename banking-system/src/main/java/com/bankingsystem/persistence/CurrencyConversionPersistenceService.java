package com.bankingsystem.persistence;

import com.bankingsystem.models.CurrencyConversion;

public interface CurrencyConversionPersistenceService {
    CurrencyConversion getLatestConversion();
    void updateConversion(CurrencyConversion conversion);

}
