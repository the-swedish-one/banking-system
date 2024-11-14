package com.bankingsystem.controller;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.mapper.ApiCurrencyConversionMapper;
import com.bankingsystem.model.ApiCurrencyConversion;
import com.bankingsystem.service.CurrencyConversionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/currency-conversion")
public class CurrencyConversionController {

    private final CurrencyConversionService currencyConversionService;
    private final ApiCurrencyConversionMapper apiCurrencyConversionMapper;

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
        return currencyConversionService.getExchangeRate(fromCurrency, toCurrency);
    }

    @PutMapping("/update-rates")
    public ApiCurrencyConversion updateExchangeRates(@RequestBody ApiCurrencyConversion apiCurrencyConversion) {
        Map<CurrencyCode, Double> newRates = apiCurrencyConversion.getExchangeRates();
        currencyConversionService.updateExchangeRates(newRates);

        // Return updated exchange rates as response
        return apiCurrencyConversionMapper.toApiModel(
                currencyConversionService.getLatestConversion());
    }
}
