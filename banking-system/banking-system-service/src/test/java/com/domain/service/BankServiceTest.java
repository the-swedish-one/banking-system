package com.domain.service;

import com.bankingsystem.exception.BankNotFoundException;
import com.domain.model.Bank;
import com.domain.persistence.BankPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @Mock
    private BankPersistenceService bankPersistenceService;

    @InjectMocks
    private BankService bankService;

    @Nested
    class CreateBankTests {

        @Test
        void createBank() {
            // Arrange
            Bank mockBank = new Bank("My Bank", "MB001");
            when(bankPersistenceService.save(any(Bank.class))).thenReturn(mockBank);

            // Act
            Bank createdBank = bankService.createBank("My Bank", "MB001");

            // Assert
            assertAll(
                    () -> assertNotNull(createdBank),
                    () -> assertEquals("MB001", createdBank.getBic()),
                    () -> assertEquals("My Bank", createdBank.getBankName())
            );
            verify(bankPersistenceService, times(1)).save(any(Bank.class));
        }

        @Test
        void createBank_NullValues() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> bankService.createBank(null, "MB001"));
            assertThrows(IllegalArgumentException.class, () -> bankService.createBank("My Bank", null));
            verify(bankPersistenceService, never()).save(any(Bank.class));
        }
    }

    @Nested
    class GetBankByBicTests {

        @Test
        void getBankByBic() {
            // Arrange
            Bank mockBank = new Bank("My Bank", "MB001");
            when(bankPersistenceService.getBankByBic("MB001")).thenReturn(mockBank);

            // Act
            Bank fetchedBank = bankService.getBankByBic("MB001");

            // Assert
            assertNotNull(fetchedBank);
            assertEquals("MB001", fetchedBank.getBic());
            assertEquals("My Bank", fetchedBank.getBankName());
            verify(bankPersistenceService, times(1)).getBankByBic("MB001");
        }

        @Test
        void getBankByBic_NotFound() {
            // Arrange
            when(bankPersistenceService.getBankByBic("INVALID")).thenThrow(new BankNotFoundException("Bank not found"));

            // Act & Assert
            assertThrows(BankNotFoundException.class, () -> bankService.getBankByBic("INVALID"));
            verify(bankPersistenceService, times(1)).getBankByBic("INVALID");
        }

    }

    @Nested
    class UpdateBankTests {

        @Test
        void updateBank() {
            // Arrange
            Bank existingBank = new Bank("Old Bank", "MB001");
            Bank updatedBank = new Bank("Updated Bank", "MB001");
            when(bankPersistenceService.getBankByBic("MB001")).thenReturn(existingBank);
            when(bankPersistenceService.save(any(Bank.class))).thenReturn(updatedBank);

            // Act
            Bank result = bankService.updateBank("MB001", "Updated Bank");

            // Assert
            assertNotNull(result);
            assertEquals("MB001", result.getBic());
            assertEquals("Updated Bank", result.getBankName());
            verify(bankPersistenceService, times(1)).getBankByBic("MB001");
            verify(bankPersistenceService, times(1)).save(any(Bank.class));
        }

        @Test
        void updateBank_NotFound() {
            // Arrange
            when(bankPersistenceService.getBankByBic("INVALID")).thenThrow(new BankNotFoundException("Bank not found"));

            // Act & Assert
            assertThrows(BankNotFoundException.class, () -> bankService.updateBank("INVALID", "Updated Bank"));
            verify(bankPersistenceService, times(1)).getBankByBic("INVALID");
            verify(bankPersistenceService, never()).save(any(Bank.class));
        }

    }
}
