package com.domain.model;

import com.bankingsystem.enums.CurrencyCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion {

    private int id;
    private Date lastUpdated;
    private Map<CurrencyCode, Double> exchangeRates;

    public CurrencyConversion(Map<CurrencyCode, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.lastUpdated = new Date();
    }
}
