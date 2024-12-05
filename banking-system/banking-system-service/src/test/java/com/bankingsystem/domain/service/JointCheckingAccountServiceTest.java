package com.bankingsystem.domain.service;

import com.bankingsystem.domain.persistence.UserPersistenceService;
import com.bankingsystem.persistence.enums.CurrencyCode;
import com.bankingsystem.persistence.exception.AccountNotFoundException;
import com.bankingsystem.domain.exception.OverdraftLimitExceededException;
import com.bankingsystem.domain.model.JointCheckingAccount;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.model.User;
import com.bankingsystem.domain.persistence.JointCheckingAccountPersistenceService;
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
public class JointCheckingAccountServiceTest {

    @Mock
    private JointCheckingAccountPersistenceService accountPersistenceService;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private JointCheckingAccountService accountService;

    @Nested
    class CreateJointCheckingAccountTests {
        @Test
        void createJointCheckingAccount() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            int userId1 = user1.getUserId();
            int userId2 = user2.getUserId();
            when(userPersistenceService.getUserById(userId1)).thenReturn(user1);
            when(userPersistenceService.getUserById(userId2)).thenReturn(user2);
            JointCheckingAccount account = TestDataFactory.createJointCheckingAccount(user1, user2);
            when(accountPersistenceService.save(account)).thenReturn(account);

            // Act
            JointCheckingAccount jointCheckingAccount = accountService.createJointCheckingAccount(account);

            // Assert
            assertNotNull(jointCheckingAccount);
            assertEquals(BigDecimal.valueOf(1000), jointCheckingAccount.getBalance());
            assertEquals(CurrencyCode.EUR, jointCheckingAccount.getCurrency());
            assertEquals(BigDecimal.valueOf(500), jointCheckingAccount.getOverdraftLimit());
            assertEquals(user1, jointCheckingAccount.getOwner());
            assertEquals(user2, jointCheckingAccount.getSecondOwner());
            verify(accountPersistenceService, times(1)).save(jointCheckingAccount);
        }

        @Test
        void createJointCheckingAccount_InvalidData() {
            JointCheckingAccount invalidAccount = new JointCheckingAccount();
            invalidAccount.setBalance(null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.createJointCheckingAccount(invalidAccount));

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
    class GetJointCheckingAccountByIdTests {
        @Test
        void getAccountById() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);

            // Act
            JointCheckingAccount retrievedAccount = accountService.getJointCheckingAccountById(jointCheckingAccount.getAccountId());

            // Assert
            assertNotNull(retrievedAccount);
            assertEquals(jointCheckingAccount, retrievedAccount);
            verify(accountPersistenceService, times(1)).getAccountById(jointCheckingAccount.getAccountId());
        }

        @Test
        void getAccountById_InvalidId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> accountService.getJointCheckingAccountById(0));

            assertEquals("Account ID must be greater than zero", exception.getMessage());
        }

        @Test
        void getAccountById_AccountNotFound() {
            when(accountPersistenceService.getAccountById(123)).thenThrow(new AccountNotFoundException("Account not found"));
            // Act & Assert
            assertThrows(AccountNotFoundException.class, () -> accountService.getJointCheckingAccountById(123));
        }
    }

    @Nested
    class GetAllJointCheckingAccounts {

        @Test
        void getAllJointCheckingAccounts() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            JointCheckingAccount jointCheckingAccount2 = TestDataFactory.createJointCheckingAccount(user1, user2);
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of(jointCheckingAccount, jointCheckingAccount2));

            // Act
            List<JointCheckingAccount> jointCheckingAccounts = accountService.getAllJointCheckingAccounts();

            // Assert
            assertNotNull(jointCheckingAccounts);
            assertEquals(2, jointCheckingAccounts.size());
            assertEquals(jointCheckingAccount, jointCheckingAccounts.get(0));
            assertEquals(jointCheckingAccount2, jointCheckingAccounts.get(1));
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }

        @Test
        void getAllJointCheckingAccounts_EmptyList() {
            // Arrange
            when(accountPersistenceService.getAllAccounts()).thenReturn(List.of());

            // Act
            List<JointCheckingAccount> jointCheckingAccounts = accountService.getAllJointCheckingAccounts();

            // Assert
            assertNotNull(jointCheckingAccounts);
            assertEquals(0, jointCheckingAccounts.size());
            verify(accountPersistenceService, times(1)).getAllAccounts();
        }
    }

    @Nested
    class UpdateJointCheckingAccountTests {

        @Test
        void updateJointCheckingAccount() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);

            when(accountPersistenceService.updateAccount(jointCheckingAccount)).thenReturn(jointCheckingAccount);

            // Act
            JointCheckingAccount updatedAccount = accountService.updateJointCheckingAccount(jointCheckingAccount);

            // Assert
            assertNotNull(updatedAccount);
            assertEquals(jointCheckingAccount, updatedAccount);
            verify(accountPersistenceService, times(1)).updateAccount(jointCheckingAccount);
        }
    }


    @Nested
    class DeleteJointCheckingAccountTests {

        @Test
        void deleteAccount() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);

            when(accountPersistenceService.deleteAccount(jointCheckingAccount.getAccountId())).thenReturn(true);

            // Act
            boolean wasDeleted = accountService.deleteJointCheckingAccount(jointCheckingAccount.getAccountId());

            // Assert
            assertTrue(wasDeleted);
            verify(accountPersistenceService, times(1)).deleteAccount(jointCheckingAccount.getAccountId());
        }

        @Test
        void deleteAccount_NotFound() {
            when(accountPersistenceService.deleteAccount(99)).thenThrow(new AccountNotFoundException("Joint Checking Account not found"));

            AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                    () -> accountService.deleteJointCheckingAccount(99));

            assertEquals("Joint Checking Account not found", exception.getMessage());
        }
    }

    @Nested
    class CheckingAccountDepositTests {

        @Test
        void deposit() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            Transaction transaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), null, jointCheckingAccount.getAccountId());

            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);
            when(transactionService.createTransaction(transaction)).thenReturn(transaction);

            // Act
            accountService.deposit(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(1500), jointCheckingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(jointCheckingAccount);
            verify(transactionService, times(1)).createTransaction(transaction);
        }

        @Test
        void deposit_NegativeAmount() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Deposit amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(jointCheckingAccount);
        }
    }

    @Nested
    class JointCheckingAccountWithdrawTests {

        @Test
        void withdraw() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(-500), jointCheckingAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(500));

            // Assert
            assertEquals(BigDecimal.valueOf(500), jointCheckingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(jointCheckingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void withdraw_NegativeAmount() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);

            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(-500)));
            assertEquals("Withdrawal failed: Amount must be greater than zero", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(jointCheckingAccount);
        }

        @Test
        void withdraw_ExceedsBalanceWithinOverdraftLimit() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(-1400), jointCheckingAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(1400));

            // Assert
            assertEquals(BigDecimal.valueOf(-400), jointCheckingAccount.getBalance());
            verify(accountPersistenceService, times(1)).updateAccount(jointCheckingAccount);
            verify(transactionService, times(1)).createTransaction(withdrawTransaction);
        }

        @Test
        void withdraw_ExceedsOverdraftLimit() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);

            // Act & Assert
            Exception exception = assertThrows(OverdraftLimitExceededException.class, () -> accountService.withdraw(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(4000)));
            assertEquals("Withdrawal failed: Overdraft limit exceeded", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(jointCheckingAccount);
        }
    }

    @Nested
    class JointCheckingAccountTransferTests {

        @Test
        void transfer() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount fromAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            JointCheckingAccount toAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
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
            List<JointCheckingAccount> updatedAccounts = accountService.transfer(
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
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount fromAccount = TestDataFactory.createJointCheckingAccount(user1, user2, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
            JointCheckingAccount toAccount = TestDataFactory.createJointCheckingAccount(user2, user2, BigDecimal.valueOf(0), CurrencyCode.USD, BigDecimal.valueOf(2000));

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
            List<JointCheckingAccount> updatedAccounts = accountService.transfer(
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
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount fromAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            JointCheckingAccount toAccount = TestDataFactory.createJointCheckingAccount(user1, user2);

            when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
            when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500), fromAccount.getAccountId(), toAccount.getAccountId()));
            assertEquals("Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

    }
}
