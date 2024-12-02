package com.bankingsystem.api.controller;

import com.bankingsystem.persistence.enums.CurrencyCode;
import com.bankingsystem.api.mapper.ApiCurrencyConversionMapper;
import com.bankingsystem.api.model.ApiCurrencyConversion;
import com.bankingsystem.domain.model.CurrencyConversion;
import com.bankingsystem.domain.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/currency-conversion")
public class CurrencyConversionController {

    private final CurrencyConversionService currencyConversionService;
    private final ApiCurrencyConversionMapper apiCurrencyConversionMapper;

    @Autowired
    public CurrencyConversionController(CurrencyConversionService currencyConversionService, ApiCurrencyConversionMapper apiCurrencyConversionMapper) {
        this.currencyConversionService = currencyConversionService;
        this.apiCurrencyConversionMapper = apiCurrencyConversionMapper;
    }

    @PostMapping("/convert")
    public BigDecimal convertAmount(@RequestParam BigDecimal amount,
                                    @RequestParam CurrencyCode fromCurrency,
                                    @RequestParam CurrencyCode toCurrency) {
        return currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);
    }

    @GetMapping("/rate")
    public double getExchangeRate(@RequestParam CurrencyCode fromCurrency,
                                  @RequestParam CurrencyCode toCurrency) {
        CurrencyConversion conversion = currencyConversionService.getLatestCurrencyConversion();
        return currencyConversionService.getExchangeRate(conversion, fromCurrency, toCurrency);
    }

    @PutMapping("/update-rates")
    public ApiCurrencyConversion updateExchangeRates(@RequestBody ApiCurrencyConversion apiCurrencyConversion) {
        Map<CurrencyCode, Double> newRates = apiCurrencyConversion.getExchangeRates();
        currencyConversionService.updateExchangeRates(newRates);

        // Return updated exchange rates as response
        return apiCurrencyConversionMapper.toApiModel(
                currencyConversionService.getLatestCurrencyConversion());
    }
}
