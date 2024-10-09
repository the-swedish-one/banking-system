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

    @Test
    void testConvertAmount() {
        // Arrange
        double amount = 100.0;
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        // Mock the conversion rates
        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);  // Assuming USD is the base
        exchangeRates.put(CurrencyCode.EUR, 0.85);

        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        double convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(85.0, convertedAmount);  // 100 USD -> 85 EUR
        verify(currencyConversionDAO, times(2)).getLatestConversion();
    }

    @Test
    void testConvertAmount_SameCurrency() {
        // Arrange
        double amount = 100.0;
        CurrencyCode currency = CurrencyCode.USD;

        // Act
        double convertedAmount = currencyConversionService.convertAmount(amount, currency, currency);

        // Assert
        assertEquals(amount, convertedAmount);
    }


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
    void testConvertAmount_UnsupportedCurrency() {
        // Arrange
        double amount = 100.0;
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
        double amount = 0.0;
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);
        exchangeRates.put(CurrencyCode.EUR, 0.85);
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        double convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(0.0, convertedAmount);  // 0 USD -> 0 EUR
    }

    @Test
    void testConvertAmount_NegativeAmount() {
        // Arrange
        double amount = -100.0;
        CurrencyCode fromCurrency = CurrencyCode.USD;
        CurrencyCode toCurrency = CurrencyCode.EUR;

        Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyCode.USD, 1.0);
        exchangeRates.put(CurrencyCode.EUR, 0.85);
        CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
        when(currencyConversionDAO.getLatestConversion()).thenReturn(conversion);

        // Act
        double convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(-85.0, convertedAmount);  // -100 USD -> -85 EUR
    }

    @Test
    void testConvertAmount_NoRatesAvailable() {
        // Arrange
        double amount = 100.0;
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
