package com.bankingsystem.services;

import com.bankingsystem.models.CurrencyCode;
import com.bankingsystem.models.CurrencyConversion;
import com.bankingsystem.persistence.dao.CurrencyConversionDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTest {

    @Mock
    private CurrencyConversionDAO currencyConversionDAO;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

//    Test Convert Amount
    @Test
    void testConvertAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        // Mock the conversion rates
        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);  // Assuming USD is the base
        exchangeRates.put(CurrencyCode.EUR, 0.85);

        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(BigDecimal.valueOf(85.00).setScale(2, RoundingMode.HALF_UP), convertedAmount);  // 100 USD -> 85 EUR
        verify(currencyConversionDAO, times(2)).getLatestConversion();
    }

    @Test
    void testConvertAmount_SameCurrency() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP);
        CurrencyCode currency = CurrencyCode.USD;

        // Act
        BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, currency, currency);

        // Assert
        assertEquals(amount, convertedAmount);
    }

    @Test
    void testConvertAmount_UnsupportedCurrency() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        CurrencyCode unsupportedCurrency = CurrencyCode.GBP;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);
        exchangeRates.put(CurrencyCode.EUR, 0.85);
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConversionService.convertAmount(amount, unsupportedCurrency, CurrencyCode.USD);
        });
    }

    @Test
    void testConvertAmount_ZeroAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(0);
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);
        exchangeRates.put(CurrencyCode.EUR, 0.85);
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), convertedAmount);  // 0 USD -> 0 EUR
    }

    @Test
    void testConvertAmount_NegativeAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(-100);
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);
        exchangeRates.put(CurrencyCode.EUR, 0.85);
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(BigDecimal.valueOf(-85.00).setScale(2, RoundingMode.HALF_UP), convertedAmount);  // -100 USD -> -85 EUR
    }

    @Test
    void testConvertAmount_NoRatesAvailable() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();  // Empty rates
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);
        });
    }

    //    Test Get Exchange Rate
    @Test
    void testGetExchangeRate_SameCurrency() {
        // Arrange
        CurrencyCode currency = CurrencyCode.USD;

        // Act
        double rate = currencyConversionService.getExchangeRate(currency, currency);

        // Assert
        assertEquals(1.0, rate);
    }

    @Test
    void testGetExchangeRate_DifferentCurrencies() {
        // Arrange
        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);  // USD as base currency
        exchangeRates.put(CurrencyCode.EUR, 0.85);  // 1 USD = 0.85 EUR

        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        double rate = currencyConversionService.getExchangeRate(CurrencyCode.USD, CurrencyCode.EUR);

        // Assert
        assertEquals(0.85, rate);
        verify(currencyConversionDAO, times(1)).getLatestConversion();
    }

    @Test
    void testGetExchangeRate_UnsupportedCurrency() {
        // Arrange
        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);  // USD as base currency
        // EUR is missing from the exchange rates

        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConversionService.getExchangeRate(CurrencyCode.USD, CurrencyCode.EUR);
        });
    }

//    Test Update Exchange Rates
    @Test
    void testUpdateExchangeRates() {
        // Arrange
        Map<CurrencyCode, Double> newRates = new HashMap<>();
        newRates.put(CurrencyCode.USD, 1.0);
        newRates.put(CurrencyCode.EUR, 0.85);
        newRates.put(CurrencyCode.GBP, 0.75);

        // Capture the argument to verify
        ArgumentCaptor<CurrencyConversion> conversionCaptor = ArgumentCaptor.forClass(CurrencyConversion.class);

        // Act
        currencyConversionService.updateExchangeRates(newRates);

        // Assert
        verify(currencyConversionDAO, times(1)).updateConversion(conversionCaptor.capture());
        CurrencyConversion capturedConversion = conversionCaptor.getValue();

        // Check that the new rates were set correctly
        assertEquals(newRates, capturedConversion.getExchangeRates());
        assertNotNull(capturedConversion.getLastUpdated());
    }
}
