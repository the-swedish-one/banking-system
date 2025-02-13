package com.bankingsystem.domain.service;

import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.enums.CurrencyCode;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.model.CheckingAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.CheckingAccountPersistenceService;
import com.bankingsystem.domain.testutils.TestDataFactory;
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
class CheckingAccountServiceTest {

    @Mock
    private CheckingAccountPersistenceService accountPersistenceService;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CheckingAccountService accountService;

    @Nested
    class CreateCheckingAccountTests {

        @Test
        void createCheckingAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            int userId = user.getUserId();
            when(userPersistenceService.getUserById(userId)).thenReturn(user);
            CheckingAccount account = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.save(account)).thenReturn(account);

            // Act
            CheckingAccount checkingAccount = accountService.createCheckingAccount(account);

            // Assert
            assertNotNull(checkingAccount);
            assertEquals(BigDecimal.valueOf(1000), checkingAccount.getBalance());
            assertEquals(CurrencyCode.EUR, checkingAccount.getCurrency());
            assertEquals(BigDecimal.valueOf(500), checkingAccount.getOverdraftLimit());
            assertEquals(user, checkingAccount.getOwner());
            verify(accountPersistenceService, times(1)).save(checkingAccount);
        }

        @Test
        void createCheckingAccount_InvalidData() {
            CheckingAccount invalidAccount = new CheckingAccount();
            invalidAccount.setBalance(null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.createCheckingAccount(invalidAccount));

            assertEquals("Account creation failed: Missing required fields", exception.getMessage());
        }
    }

    @Nested
    class GenerateIBANTests {
        @Test
        void generateIBAN_ValidCountry_ReturnsIBAN() {
            String country = "US";

            String iban = accountService.generateIBAN(country);

            assertNotNull(iban);
            assertTrue(iban.startsWith("US"));
            assertEquals(16, iban.length()); // 2 letters for country + 14 digits
            assertTrue(iban.substring(2).chars().allMatch(Character::isDigit));
        }

        @Test
        void generateIBAN_NullCountry_ThrowsException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN(null));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_EmptyCountry_ThrowsException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN(""));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_ShortCountryCode_ThrowsException() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.generateIBAN("A"));

            assertEquals("Country is null or less than 2 characters", exception.getMessage());
        }

        @Test
        void generateIBAN_CountryCodeLowerCase_ConvertsToUpperCase() {
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
    class GetCheckingAccountByIdTests {
        @Test
        void getAccountById() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act
            CheckingAccount retrievedAccount = accountService.getCheckingAccountById(checkingAccount.getAccountId());

            // Assert
            assertNotNull(retrievedAccount);
            assertEquals(checkingAccount, retrievedAccount);
            verify(accountPersistenceService, times(1)).getAccountById(checkingAccount.getAccountId());
        }

        @Test
        void getAccountById_InvalidId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.getCheckingAccountById(0));

            assertEquals("Account ID must be greater than zero", exception.getMessage());
        }

        @Test
        void getAccountById_AccountNotFound() {
            when(accountPersistenceService.getAccountById(123)).thenThrow(new AccountNotFoundException("Checking Account not found"));
            // Act & Assert
            assertThrows(AccountNotFoundException.class, () -> accountService.getCheckingAccountById(123));
        }
    }

    @Nested
    class GetAllCheckingAccounts {

        @Test
        void getAllCheckingAccounts() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            CheckingAccount checkingAccount2 = TestDataFactory.createCheckingAccount(user);
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of(checkingAccount, checkingAccount2));

            // Act
            List<CheckingAccount> checkingAccounts = accountService.getAllCheckingAccounts();

            // Assert
            assertNotNull(checkingAccounts);
            assertEquals(2, checkingAccounts.size());
            assertEquals(checkingAccount, checkingAccounts.get(0));
            assertEquals(checkingAccount2, checkingAccounts.get(1));
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }

        @Test
        void getAllCheckingAccounts_EmptyList() {
            // Arrange
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of());

            // Act
            List<CheckingAccount> checkingAccounts = accountService.getAllCheckingAccounts();

            // Assert
            assertNotNull(checkingAccounts);
            assertEquals(0, checkingAccounts.size());
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }
    }


    @Nested
    class UpdateCheckingAccountTests {

        @Test
        void updateCheckingAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            when(accountPersistenceService.updateAccount(checkingAccount)).thenReturn(checkingAccount);

            // Act
            CheckingAccount updatedAccount = accountService.updateCheckingAccount(checkingAccount);

            // Assert
            assertNotNull(updatedAccount);
            assertEquals(checkingAccount, updatedAccount);
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
        }
    }


    @Nested
    class DeleteCheckingAccountTests {

        @Test
        void deleteAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.deleteAccount(checkingAccount.getAccountId())).thenReturn(true);

            // Act
            boolean wasDeleted = accountService.deleteCheckingAccount(checkingAccount.getAccountId());

            // Assert
            assertTrue(wasDeleted);
            verify(accountPersistenceService, times(1)).deleteAccount(checkingAccount.getAccountId());
        }

        @Test
        void deleteAccount_NotFound() {
            when(accountPersistenceService.deleteAccount(99)).thenThrow(new AccountNotFoundException("Checking Account not found"));

            AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                    () -> accountService.deleteCheckingAccount(99));

            assertEquals("Checking Account not found", exception.getMessage());
        }
    }

    @Nested
    class CheckingAccountDepositTests {

        @Test
        void deposit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction transaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), null, checkingAccount.getIban());

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
            when(transactionService.createTransaction(transaction)).thenReturn(transaction);

            // Act
            accountService.deposit(checkingAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(1500), checkingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
            verify(transactionService, times(1)).createTransaction(transaction);
        }

        @Test
        void deposit_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Deposit failed: Amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }
    }

    @Nested
    class CheckingAccountWithdrawTests {

        @Test
        void withdraw() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(-500).setScale(2, RoundingMode.HALF_UP), checkingAccount.getIban(), null);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), checkingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void withdraw_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Withdraw failed: Amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }

        @Test
        void withdraw_ExceedsBalanceWithinOverdraftLimit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(-1400).setScale(2, RoundingMode.HALF_UP), checkingAccount.getIban(), null);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(1400));

            // Assert
            assertEquals(BigDecimal.valueOf(-400).setScale(2, RoundingMode.HALF_UP), checkingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void withdraw_ExceedsOverdraftLimit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenThrow(new OverdraftLimitExceededException("Withdraw Failed: Overdraft limit exceeded"));

            // Act & Assert
            Exception exception = assertThrows(OverdraftLimitExceededException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(4000)));
            assertEquals("Withdraw Failed: Overdraft limit exceeded", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }
    }

    @Nested
    class CheckingAccountTransferTests {

        @Test
        void transfer() {
            // Arrange
            User user = TestDataFactory.createUser();
            String fromIban = "IBAN12345";
            String toIban = "IBAN67890";
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user); // Initial balance: 1000
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user); // Initial balance: 1000

            fromAccount.setIban(fromIban);
            toAccount.setIban(toIban);

            // Create expected updated accounts
            CheckingAccount updatedFromAccount = TestDataFactory.createCheckingAccount(user);
            updatedFromAccount.setBalance(BigDecimal.valueOf(500)); // Deducted 500 from the original balance
            CheckingAccount updatedToAccount = TestDataFactory.createCheckingAccount(user);
            updatedToAccount.setBalance(BigDecimal.valueOf(1500)); // Added 500 to the original balance

            BigDecimal transferAmount = BigDecimal.valueOf(500);

            when(accountPersistenceService.getAccountByIban(fromIban)).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountByIban(toIban)).thenReturn(toAccount);
            when(accountPersistenceService.updateAccount(fromAccount)).thenReturn(updatedFromAccount);
            when(accountPersistenceService.updateAccount(toAccount)).thenReturn(updatedToAccount);

            // Act
            List<CheckingAccount> updatedAccounts = accountService.transfer(BigDecimal.valueOf(500), fromIban, toIban);

            // Assert
            assertNotNull(updatedAccounts);
            assertEquals(2, updatedAccounts.size());
            assertEquals(BigDecimal.valueOf(500), updatedAccounts.get(0).getBalance()); // From Account
            assertEquals(BigDecimal.valueOf(1500), updatedAccounts.get(1).getBalance()); // To Account

            verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(1)).updateAccount(toAccount);
        }


        @Test
        void transfer_DifferentCurrencies() {
            // Arrange
            User user = TestDataFactory.createUser();
            String fromIban = "IBAN12345";
            String toIban = "IBAN67890";
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(0), CurrencyCode.USD, BigDecimal.valueOf(2000));

            fromAccount.setIban(fromIban);
            toAccount.setIban(toIban);

            BigDecimal amount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP);

            // Create updated accounts to mock the persistence service updates
            CheckingAccount updatedFromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(500), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            CheckingAccount updatedToAccount = TestDataFactory.createCheckingAccount(user, convertedAmount, CurrencyCode.USD, BigDecimal.valueOf(2000));

            // Transactions for from and to accounts
            Transaction fromTransaction = new Transaction(amount.negate(), fromIban, toIban);
            Transaction toTransaction = new Transaction(convertedAmount, fromIban, toIban);

            // Mock dependencies
            when(accountPersistenceService.getAccountByIban(fromIban)).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountByIban(toIban)).thenReturn(toAccount);
            when(accountPersistenceService.updateAccount(fromAccount)).thenReturn(updatedFromAccount);
            when(accountPersistenceService.updateAccount(toAccount)).thenReturn(updatedToAccount);
            when(currencyConversionService.convertAmount(amount, CurrencyCode.EUR, CurrencyCode.USD)).thenReturn(convertedAmount);
            when(transactionService.createTransaction(fromTransaction)).thenReturn(fromTransaction);
            when(transactionService.createTransaction(toTransaction)).thenReturn(toTransaction);

            // Act
            List<CheckingAccount> updatedAccounts = accountService.transfer(amount, fromIban, toIban);

            // Assert
            assertNotNull(updatedAccounts);
            assertEquals(2, updatedAccounts.size());
            assertEquals(BigDecimal.valueOf(500), updatedAccounts.get(0).getBalance()); // From Account
            assertEquals(BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP), updatedAccounts.get(1).getBalance()); // To Account with converted amount
            verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(1)).updateAccount(toAccount);
            verify(transactionService, times(1)).createTransaction(fromTransaction);
            verify(transactionService, times(1)).createTransaction(toTransaction);
        }


        @Test
        void transfer_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            String fromIban = "IBAN12345";
            String toIban = "IBAN67890";
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, BigDecimal.valueOf(2000));

            fromAccount.setIban(fromIban);
            toAccount.setIban(toIban);

            when(accountPersistenceService.getAccountByIban(fromIban)).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountByIban(toIban)).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500),fromIban, toIban));
            assertEquals("Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

    }

}

