package com.bankingsystem.persistence;

import com.bankingsystem.model.CurrencyConversion;

public interface CurrencyConversionPersistenceService {
    CurrencyConversion getLatestConversion();
    void updateConversion(CurrencyConversion conversion);

}
