package com.bankingsystem.domain.service;

import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.enums.CurrencyCode;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.InsufficientFundsException;
import com.bankingsystem.domain.model.SavingsAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.SavingsAccountPersistenceService;
import com.bankingsystem.domain.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SavingsAccountServiceTest {

    @Mock
    private SavingsAccountPersistenceService accountPersistenceService;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private SavingsAccountService accountService;

        @Test
        void createSavingsAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            int userId = user.getUserId();
            when(userPersistenceService.getUserById(userId)).thenReturn(user);
            SavingsAccount account = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.save(account)).thenReturn(account);

            // Act
            SavingsAccount savingsAccount = accountService.createSavingsAccount(account);

            // Assert
            assertNotNull(savingsAccount);
            assertEquals(BigDecimal.valueOf(1000), savingsAccount.getBalance());
            assertEquals(CurrencyCode.EUR, savingsAccount.getCurrency());
            assertEquals(2, savingsAccount.getInterestRatePercentage());
            assertEquals(user, savingsAccount.getOwner());
            verify(accountPersistenceService, times(1)).save(savingsAccount);
        }

        @Test
        void createSavingsAccount_InvalidData() {
            SavingsAccount invalidAccount = new SavingsAccount();
            invalidAccount.setBalance(null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.createSavingsAccount(invalidAccount));

            assertEquals("Account creation failed: Missing required fields", exception.getMessage());
        }

        @Test
        void generateIBAN_ValidCountry() {
            String country = "US";

            String iban = accountService.generateIBAN(country);

            assertNotNull(iban);
            assertTrue(iban.startsWith("US"));
            assertEquals(16, iban.length()); // 2 letters for country + 14 digits
            assertTrue(iban.substring(2).chars().allMatch(Character::isDigit));
        }

        @Test
        void generateIBAN_NullCountry() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN(null));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_EmptyCountry() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN(""));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_ShortCountryCode() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN("A"));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_CountryCodeLowerCase() {
            String country = "gb";

            String iban = accountService.generateIBAN(country);

            assertNotNull(iban);
            assertTrue(iban.startsWith("GB"));
            assertEquals(16, iban.length());
        }

        @Test
        void generateIBAN_GeneratesUniqueIBANs() {
            String country = "FR";

            String iban1 = accountService.generateIBAN(country);
            String iban2 = accountService.generateIBAN(country);

            assertNotEquals(iban1, iban2);
        }

        @Test
        void getAccountById() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act
            SavingsAccount retrievedAccount = accountService.getSavingsAccountById(savingsAccount.getAccountId());

            // Assert
            assertNotNull(retrievedAccount);
            assertEquals(savingsAccount, retrievedAccount);
            verify(accountPersistenceService, times(1)).getAccountById(savingsAccount.getAccountId());
        }

        @Test
        void getAccountById_InvalidId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.getSavingsAccountById(0));

            assertEquals("Account ID must be greater than zero", exception.getMessage());
        }

        @Test
        void getAccountById_AccountNotFound() {
            when(accountPersistenceService.getAccountById(123)).thenThrow(new AccountNotFoundException("Savings Account not found"));

            // Act & Assert
            assertThrows(AccountNotFoundException.class, () -> accountService.getSavingsAccountById(123));
        }

        @Test
        void getAllSavingsAccounts() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);
            SavingsAccount savingsAccount2 = TestDataFactory.createSavingsAccount(user);
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of(savingsAccount, savingsAccount2));

            // Act
            List<SavingsAccount> savingsAccounts = accountService.getAllSavingsAccounts();

            // Assert
            assertNotNull(savingsAccounts);
            assertEquals(2, savingsAccounts.size());
            assertEquals(savingsAccount, savingsAccounts.get(0));
            assertEquals(savingsAccount2, savingsAccounts.get(1));
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }

        @Test
        void getAllSavingsAccounts_EmptyList() {
            // Arrange
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of());

            // Act
            List<SavingsAccount> savingsAccounts = accountService.getAllSavingsAccounts();

            // Assert
            assertNotNull(savingsAccounts);
            assertEquals(0, savingsAccounts.size());
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }

        @Test
        void updateSavingsAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);
            when(accountPersistenceService.updateAccount(savingsAccount)).thenReturn(savingsAccount);

            // Act
            SavingsAccount updatedAccount = accountService.updateSavingsAccount(savingsAccount);

            // Assert
            assertNotNull(updatedAccount);
            assertEquals(savingsAccount, updatedAccount);
            verify(accountPersistenceService, times(1)).updateAccount(savingsAccount);
        }

        @Test
        void deleteAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.deleteAccount(savingsAccount.getAccountId())).thenReturn(true);

            // Act
            boolean wasDeleted = accountService.deleteSavingsAccount(savingsAccount.getAccountId());

            // Assert
            assertTrue(wasDeleted);
            verify(accountPersistenceService, times(1)).deleteAccount(savingsAccount.getAccountId());
        }

        @Test
        void deleteAccount_NotFound() {
            when(accountPersistenceService.deleteAccount(99)).thenThrow(new AccountNotFoundException("Savings Account not found"));

            AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                    () -> accountService.deleteSavingsAccount(99));

            assertEquals("Savings Account not found", exception.getMessage());
        }

        @Test
        void deposit() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);
            Transaction transaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), null, savingsAccount.getAccountId());

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);
            when(transactionService.createTransaction(transaction)).thenReturn(transaction);

            // Act
            accountService.deposit(savingsAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(1500), savingsAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(savingsAccount);
            verify(transactionService, times(1)).createTransaction(transaction);
        }

        @Test
        void deposit_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(savingsAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Deposit amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }

        @Test
        void withdraw() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(-500), savingsAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(500), savingsAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(savingsAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void withdraw_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Withdraw amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }

        @Test
        void withdraw_InsufficientFunds() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(2000)));
            assertEquals("Withdrawal failed: Insufficient funds", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }

    @Test
    void transfer() {
        // Arrange
        User user = TestDataFactory.createUser();
        SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user);
        SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user);
        fromAccount.setAccountId(1);
        toAccount.setAccountId(2);

        BigDecimal transferAmount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
        Transaction transferTransactionFrom = TestDataFactory.createTransaction(
                transferAmount.negate(), fromAccount.getAccountId(), toAccount.getAccountId());
        Transaction transferTransactionTo = TestDataFactory.createTransaction(
                transferAmount, fromAccount.getAccountId(), toAccount.getAccountId());

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
        when(transactionService.createTransaction(transferTransactionFrom)).thenReturn(transferTransactionFrom);
        when(transactionService.createTransaction(transferTransactionTo)).thenReturn(transferTransactionTo);
        when(accountPersistenceService.updateAccount(fromAccount)).thenReturn(fromAccount);
        when(accountPersistenceService.updateAccount(toAccount)).thenReturn(toAccount);

        // Act
        List<SavingsAccount> updatedAccounts = accountService.transfer(
                transferAmount, fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        assertEquals(2, updatedAccounts.size());
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), updatedAccounts.get(0).getBalance());
        assertEquals(BigDecimal.valueOf(1500).setScale(2, RoundingMode.HALF_UP), updatedAccounts.get(1).getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(1)).updateAccount(toAccount);
        verify(transactionService, times(1)).createTransaction(transferTransactionFrom);
        verify(transactionService, times(1)).createTransaction(transferTransactionTo);
    }

    @Test
    void transfer_DifferentCurrencies() {
        // Arrange
        User user = TestDataFactory.createUser();
        SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 2);
        SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.USD, 2);

        BigDecimal transferAmount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP);

        Transaction transferTransactionFrom = TestDataFactory.createTransaction(
                transferAmount.negate(), fromAccount.getAccountId(), toAccount.getAccountId());
        Transaction transferTransactionTo = TestDataFactory.createTransaction(
                convertedAmount, fromAccount.getAccountId(), toAccount.getAccountId());

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
        when(currencyConversionService.convertAmount(transferAmount, CurrencyCode.EUR, CurrencyCode.USD)).thenReturn(convertedAmount);
        when(transactionService.createTransaction(transferTransactionFrom)).thenReturn(transferTransactionFrom);
        when(transactionService.createTransaction(transferTransactionTo)).thenReturn(transferTransactionTo);
        when(accountPersistenceService.updateAccount(fromAccount)).thenReturn(fromAccount);
        when(accountPersistenceService.updateAccount(toAccount)).thenReturn(toAccount);

        // Act
        List<SavingsAccount> updatedAccounts = accountService.transfer(
                transferAmount, fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        assertEquals(2, updatedAccounts.size());
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), updatedAccounts.get(0).getBalance());
        assertEquals(BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP), updatedAccounts.get(1).getBalance()); // Converted amount
        verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(1)).updateAccount(toAccount);
        verify(transactionService, times(1)).createTransaction(transferTransactionFrom);
        verify(transactionService, times(1)).createTransaction(transferTransactionTo);
    }


    @Test
        void transfer_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 2);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 2);

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

        @Test
        void transfer_InsufficientFunds() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.transfer(BigDecimal.valueOf(2000), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Transfer failed: Insufficient funds", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

        @Test
        void addInterest() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            // Act
            accountService.addInterest(savingsAccount);

            // Assert
            assertEquals(BigDecimal.valueOf(1020.00).setScale(2, RoundingMode.HALF_UP), savingsAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(savingsAccount);
        }
}
