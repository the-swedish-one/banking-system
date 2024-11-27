package com.bankingsystem.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.model.CheckingAccount;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.User;
import com.bankingsystem.persistence.CheckingAccountPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
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
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CheckingAccountService accountService;

    @Nested
    class CreateCheckingAccountTests {

        @Test
        void testCreateCheckingAccount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount account = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.save(account)).thenReturn(account);

            // Act
            CheckingAccount checkingAccount = accountService.createCheckingAccount(account);

            // Assert
            assertNotNull(checkingAccount);
            assertEquals(BigDecimal.valueOf(1000), checkingAccount.getBalance());
            assertEquals(CurrencyCode.EUR, checkingAccount.getCurrency());
            assertEquals(BigDecimal.valueOf(1000), checkingAccount.getOverdraftLimit());
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
    class GetCheckingAccountByIdTests {
        @Test
        void testGetAccountById() {
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
        void getCheckingAccountById_InvalidId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.getCheckingAccountById(0));

            assertEquals("Account ID must be greater than zero", exception.getMessage());
        }

        @Test
        void testGetAccountById_AccountNotFound() {
            // Act & Assert
            assertThrows(AccountNotFoundException.class, () -> accountService.getCheckingAccountById(123));
        }

    }

    @Nested
    class GetAllCheckingAccounts {

        @Test
        void testGetAllCheckingAccounts() {
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
        void testGetAllCheckingAccounts_EmptyList() {
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
        void testUpdateCheckingAccount() {
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
        void testDeleteAccount() {
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
        void deleteCheckingAccount_NonExistingId_ThrowsException() {
            when(accountPersistenceService.deleteAccount(99)).thenThrow(new AccountNotFoundException("Checking Account not found"));

            AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                    () -> accountService.deleteCheckingAccount(99));

            assertEquals("Checking Account not found", exception.getMessage());
        }

        @Test
        void testDeleteAccount_NotFound() {
            // Arrange
            int accountId = 123;
            when(accountPersistenceService.deleteAccount(accountId)).thenReturn(false);

            // Act
            boolean wasDeleted = accountService.deleteCheckingAccount(accountId);

            // Assert
            assertFalse(wasDeleted);
            verify(accountPersistenceService, times(1)).deleteAccount(accountId);
        }
    }

    @Nested
    class CheckingAccountDepositTests {

        @Test
        void testDeposit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction transaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), null, checkingAccount.getAccountId());

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
        void testDeposit_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }
    }

    @Nested
    class CheckingAccountWithdrawTests {

        @Test
        void testWithdraw() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), checkingAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(500), checkingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void testWithdraw_ExceedsBalanceWithinOverdraftLimit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(300), checkingAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(300));

            // Assert
            assertEquals(BigDecimal.valueOf(-200), checkingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void testWithdraw_ExceedsOverdraftLimit() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);
            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act & Assert
            Exception exception = assertThrows(OverdraftLimitExceededException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(400)));
            assertEquals("Withdraw Failed: Overdraft limit exceeded", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }

        @Test
        void testWithdraw_NegativeAmount() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user);

            when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
        }
    }

    @Nested
    class CheckingAccountTransferTests {

        @Test
        void testTransfer() {
            // Arrange
            User user = TestDataFactory.createUser();
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user);
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user);
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
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(0), CurrencyCode.USD, BigDecimal.valueOf(2000));

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
            CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, BigDecimal.valueOf(2000));

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

    }

}

