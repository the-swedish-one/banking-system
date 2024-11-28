package com.domain.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.AccountNotFoundException;
import com.domain.exception.InsufficientFundsException;
import com.domain.model.SavingsAccount;
import com.domain.model.Transaction;
import com.domain.model.User;
import com.domain.persistence.SavingsAccountPersistenceService;
import com.domain.testutils.TestDataFactory;
import org.junit.jupiter.api.Nested;
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
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private SavingsAccountService accountService;

    @Nested
    class CreateSavingsAccountTests {

        @Test
        void createSavingsAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
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
    }

    @Nested
    class GenerateIBANTests {
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
    }

    @Nested
    class GetSavingsAccountByIdTests {
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
            // Act & Assert
            assertThrows(AccountNotFoundException.class, () -> accountService.getSavingsAccountById(123));
        }
    }

    @Nested
    class GetAllSavingsAccounts {
        @Test
        void testGetAllSavingsAccounts() {
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
        void testGetAllSavingsAccounts_EmptyList() {
            // Arrange
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of());

            // Act
            List<SavingsAccount> savingsAccounts = accountService.getAllSavingsAccounts();

            // Assert
            assertNotNull(savingsAccounts);
            assertEquals(0, savingsAccounts.size());
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }
    }

    @Nested
    class UpdateSavingsAccountTests {
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
    }

    @Nested
    class DeleteSavingsAccountTests {
        @Test
        void testDeleteAccount() {
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
    }

    @Nested
    class SavingsAccountDepositTests {
        @Test
        void testDeposit() {
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
        void testDeposit_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(savingsAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }
    }

    @Nested
    class SavingsAccountWithdrawTests {
        @Test
        void testWithdraw() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), savingsAccount.getAccountId(), null);

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
        void testWithdraw_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }

        @Test
        void testWithdraw_InsufficientFunds() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

            // Act & Assert
            Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(200)));
            assertEquals("Withdraw Filed: Insufficient funds", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
        }
    }

    @Nested
    class SavingsAccountTransferTests {

        @Test
        void testTransfer() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user);
            Transaction transferTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
            when(transactionService.createTransaction(transferTransaction)).thenReturn(transferTransaction);

            // Act
            accountService.transfer(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());

            // Assert
            assertEquals(BigDecimal.valueOf(500), fromAccount.getBalance());
            assertEquals(BigDecimal.valueOf(500), toAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(1)).updateAccount(toAccount);
            verify(transactionService, times(1)).createTransaction(transferTransaction);
        }

        @Test
        void testTransfer_DifferentCurrencies() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 2);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.USD, 2);

            BigDecimal amount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP);

            Transaction fromTransaction = TestDataFactory.createTransaction(amount, fromAccount.getAccountId(), toAccount.getAccountId());
            Transaction toTransaction = TestDataFactory.createTransaction(convertedAmount, fromAccount.getAccountId(), toAccount.getAccountId());

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
            when(currencyConversionService.convertAmount(amount, CurrencyCode.EUR, CurrencyCode.USD)).thenReturn(convertedAmount);
            when(transactionService.createTransaction(fromTransaction)).thenReturn(fromTransaction);
            when(transactionService.createTransaction(toTransaction)).thenReturn(toTransaction);

            // Act
            accountService.transfer(amount, fromAccount.getAccountId(), toAccount.getAccountId());

            // Assert
            assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), fromAccount.getBalance());
            assertEquals(BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP), toAccount.getBalance());  // Converted amount
            verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(1)).updateAccount(toAccount);
            verify(transactionService, times(1)).createTransaction(fromTransaction);
            verify(transactionService, times(1)).createTransaction(toTransaction);
        }

        @Test
        void testTransfer_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 2);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 2);

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

        @Test
        void testTransfer_InsufficientFunds() {
            // Arrange
            User user = TestDataFactory.createUser();
            SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user);
            SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user);

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.transfer(BigDecimal.valueOf(2000), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Withdraw Filed: Insufficient funds", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }
    }

    @Nested
    class AddInterestTests {
        @Test
        void testAddInterest() {
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
}
