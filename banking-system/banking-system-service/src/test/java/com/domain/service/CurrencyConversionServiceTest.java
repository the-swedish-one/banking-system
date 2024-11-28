package com.domain.service;

import com.bankingsystem.enums.CurrencyCode;
import com.domain.model.CurrencyConversion;
import com.domain.persistence.CurrencyConversionPersistenceService;
import org.junit.jupiter.api.Nested;
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
    private CurrencyConversionPersistenceService currencyConversionPersistenceService;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;


    @Nested
    class ConvertAmountTests {
        @Test
        void testConvertAmount() {
            BigDecimal amount = BigDecimal.valueOf(100);
            CurrencyCode fromCurrency = CurrencyCode.USD;
            CurrencyCode toCurrency = CurrencyCode.EUR;

            Map<CurrencyCode, Double> rates = Map.of(
                    CurrencyCode.USD, 1.0,
                    CurrencyCode.EUR, 0.85
            );
            CurrencyConversion conversion = new CurrencyConversion(rates);

            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

            BigDecimal convertedAmount = currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);

            assertEquals(BigDecimal.valueOf(85.00).setScale(2, RoundingMode.HALF_UP), convertedAmount);
            verify(currencyConversionPersistenceService, times(2)).getLatestConversion();
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

            Map<CurrencyCode, Double> rates = Map.of(
                    CurrencyCode.USD, 1.0,
                    CurrencyCode.EUR, 0.85
            );
            CurrencyConversion conversion = new CurrencyConversion(rates);
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

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
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

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
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

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
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                currencyConversionService.convertAmount(amount, fromCurrency, toCurrency);
            });
        }

    }

    @Nested
    class GetExchangeRateTests {

        @Test
        void testGetExchangeRate_SameCurrency() {
            // Arrange
            CurrencyCode currency = CurrencyCode.USD;
            Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
            exchangeRates.put(CurrencyCode.USD, 1.0);  // USD as base currency
            exchangeRates.put(CurrencyCode.EUR, 0.85);  // 1 USD = 0.85 EUR

            CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

            // Act
            double rate = currencyConversionService.getExchangeRate(conversion, currency, currency);

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
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

            // Act
            double rate = currencyConversionService.getExchangeRate(conversion, CurrencyCode.USD, CurrencyCode.EUR);

            // Assert
            assertEquals(0.85, rate);
            verify(currencyConversionPersistenceService, times(1)).getLatestConversion();
        }

        @Test
        void testGetExchangeRate_UnsupportedCurrency() {
            // Arrange
            Map<CurrencyCode, Double> exchangeRates = new HashMap<>();
            exchangeRates.put(CurrencyCode.USD, 1.0);  // USD as base currency
            // EUR is missing from the exchange rates

            CurrencyConversion conversion = new CurrencyConversion(exchangeRates);
            when(currencyConversionPersistenceService.getLatestConversion()).thenReturn(conversion);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                currencyConversionService.getExchangeRate(conversion, CurrencyCode.USD, CurrencyCode.EUR);
            });
        }
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
        verify(currencyConversionPersistenceService, times(1)).updateConversion(conversionCaptor.capture());
        CurrencyConversion capturedConversion = conversionCaptor.getValue();

        // Check that the new rates were set correctly
        assertEquals(newRates, capturedConversion.getExchangeRates());
        assertNotNull(capturedConversion.getLastUpdated());
    }
}
