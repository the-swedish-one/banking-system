package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.AccountNotFoundException;
import com.bankingsystem.models.exceptions.BankNotFoundException;
import com.bankingsystem.models.exceptions.UserNotFoundException;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void testCreateBank() {
        // Act
        Bank createdBank = bankService.createBank("My Bank", "MB001");

        // Assert
        assertNotNull(createdBank);
        assertEquals("MB001", createdBank.getBic());
        verify(bankPersistenceService, times(1)).createBank(any(Bank.class));
    }

//    Test Get All Bank Users
    @Test
    void testGetAllBankUsers() {
        // Arrange
        String bic = "MB001";
        Bank bank = TestDataFactory.createBank("My Bank", bic);
        User user1 = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        User user2 = TestDataFactory.createUser("John", "Smith", "js@gmail.com");

        bank.getUsers().add(user1);
        bank.getUsers().add(user2);

        when(bankPersistenceService.getBankByBic(bic)).thenReturn(bank);

        // Act
        String result = bankService.getAllBankUsers(bic);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Users of bank My Bank:"));
        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("John Smith"));
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

    @Test
    void testGetAllBankUsers_BankNotFound() {
        // Arrange
        String bic = "INVALID_BIC";
        when(bankPersistenceService.getBankByBic(bic)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(BankNotFoundException.class, () -> bankService.getAllBankUsers(bic));
        assertEquals("Bank not found", exception.getMessage());
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

    @Test
    void testGetAllBankUsers_NoUsers() {
        // Arrange
        String bic = "MB001";
        Bank bank = TestDataFactory.createBank("My Bank", bic);

        when(bankPersistenceService.getBankByBic(bic)).thenReturn(bank);

        // Act & Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> bankService.getAllBankUsers(bic));
        assertEquals("No users found", exception.getMessage());
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

//    Test Get All Bank Accounts
    @Test
    void testGetAllBankAccounts() {
        // Arrange
        String bic = "MB001";
        Bank bank = TestDataFactory.createBank("My Bank", bic);
        User user1 = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        User user2 = TestDataFactory.createUser("John", "Smith", "js@gmail.com");

        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user1, 1000, CurrencyCode.EUR, 500);
        SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user2, 2000, CurrencyCode.USD, 2.0);

        bank.getAccounts().add(checkingAccount);
        bank.getAccounts().add(savingsAccount);

        when(bankPersistenceService.getBankByBic(bic)).thenReturn(bank);

        // Act
        String result = bankService.getAllBankAccounts(bic);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Accounts of bank My Bank:"));
        assertTrue(result.contains(checkingAccount.toString()));
        assertTrue(result.contains(savingsAccount.toString()));
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

    @Test
    void testGetAllBankAccounts_BankNotFound() {
        // Arrange
        String bic = "INVALID_BIC";
        when(bankPersistenceService.getBankByBic(bic)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(BankNotFoundException.class, () -> bankService.getAllBankAccounts(bic));
        assertEquals("Bank not found", exception.getMessage());
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

    @Test
    void testGetAllBankAccounts_NoAccounts() {
        // Arrange
        String bic = "MB001";
        Bank bank = TestDataFactory.createBank("My Bank", bic);

        when(bankPersistenceService.getBankByBic(bic)).thenReturn(bank);

        // Act & Assert
        Exception exception = assertThrows(AccountNotFoundException.class, () -> bankService.getAllBankAccounts(bic));
        assertEquals("No accounts found", exception.getMessage());
        verify(bankPersistenceService, times(1)).getBankByBic(bic);
    }

}
