package com.bankingsystem.api.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCurrencyConversion {

    private int id;
    private Date lastUpdated;
    private Map<CurrencyCode, Double> exchangeRates;

    public ApiCurrencyConversion(Map<CurrencyCode, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.lastUpdated = new Date();
    }
}
