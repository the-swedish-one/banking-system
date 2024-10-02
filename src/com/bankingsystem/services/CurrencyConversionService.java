package com.bankingsystem.services;

import com.bankingsystem.persistence.dao.CurrencyConversionDAO;

public class CurrencyConversionService {

    private CurrencyConversionDAO currencyConversionDAO = new CurrencyConversionDAO();

//    public Set<CurrencyCode> getSupportedCurrencies() {
//        return currencyConversionDAO.exchangeRates.keySet();
//    }
//
//    public boolean isCurrencySupported(CurrencyCode currency) {
//        return (exchangeRates.get(currency) != null);
//    }
}
