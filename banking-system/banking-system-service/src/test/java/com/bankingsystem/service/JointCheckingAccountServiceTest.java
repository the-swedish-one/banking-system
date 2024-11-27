package com.bankingsystem.service;

import com.bankingsystem.enums.CurrencyCode;
import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.OverdraftLimitExceededException;
import com.bankingsystem.model.JointCheckingAccount;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.User;
import com.bankingsystem.persistence.JointCheckingAccountPersistenceService;
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
public class JointCheckingAccountServiceTest {

    @Mock
    private JointCheckingAccountPersistenceService accountPersistenceService;

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
            assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
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
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), jointCheckingAccount.getAccountId(), null);

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
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(jointCheckingAccount);
        }

        @Test
        void withdraw_ExceedsBalanceWithinOverdraftLimit() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount jointCheckingAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(300), jointCheckingAccount.getAccountId(), null);

            when(accountPersistenceService.getAccountById(jointCheckingAccount.getAccountId())).thenReturn(jointCheckingAccount);
            when(transactionService.createTransaction(withdrawTransaction)).thenReturn(withdrawTransaction);

            // Act
            accountService.withdraw(jointCheckingAccount.getAccountId(), BigDecimal.valueOf(300));

            // Assert
            assertEquals(BigDecimal.valueOf(-200), jointCheckingAccount.getBalance());
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
            assertEquals("Withdraw Failed: Overdraft limit exceeded", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(jointCheckingAccount);
        }
    }

    @Nested
    class CheckingAccountTransferTests {

        @Test
        void transfer() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount fromAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            JointCheckingAccount toAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
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
        void transfer_DifferentCurrencies() {
            // Arrange
            User user1 = TestDataFactory.createUser();
            User user2 = TestDataFactory.createUser();
            JointCheckingAccount fromAccount = TestDataFactory.createJointCheckingAccount(user1, user2);
            JointCheckingAccount toAccount = TestDataFactory.createJointCheckingAccount(user1, user2);

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
            assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
            verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
            verify(accountPersistenceService, times(0)).updateAccount(toAccount);
        }

    }
}
