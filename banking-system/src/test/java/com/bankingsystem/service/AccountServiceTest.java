package com.bankingsystem.service;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.AccountNotFoundException;
import com.bankingsystem.models.exceptions.InsufficientFundsException;
import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;
import com.bankingsystem.persistence.AccountPersistenceService;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.persistence.UserPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountPersistenceService accountPersistenceService;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private BankPersistenceService bankPersistenceService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

//    Create Accounts
    @Test
    void testCreateCheckingAccount() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");

        // Act
        CheckingAccount checkingAccount = accountService.createCheckingAccount(bank, user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        // Assert
        assertNotNull(checkingAccount);
        assertEquals(BigDecimal.valueOf(1000), checkingAccount.getBalance());
        assertEquals(CurrencyCode.EUR, checkingAccount.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), checkingAccount.getOverdraftLimit());
        assertEquals(user, checkingAccount.getOwner());
        verify(accountPersistenceService, times(1)).save(checkingAccount);
        verify(userPersistenceService, times(1)).updateUser(user);
        verify(bankPersistenceService, times(1)).updateBank(bank);
    }

    @Test
    void testCreateSavingsAccount() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");

        // Act
        SavingsAccount savingsAccount = accountService.createSavingsAccount(bank, user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 1.5);

        // Assert
        assertNotNull(savingsAccount);
        assertEquals(BigDecimal.valueOf(1000), savingsAccount.getBalance());
        assertEquals(CurrencyCode.EUR, savingsAccount.getCurrency());
        assertEquals(1.5, savingsAccount.getInterestRatePercentage());
        assertEquals(user, savingsAccount.getOwner());
        verify(userPersistenceService, times(1)).updateUser(user);
        verify(bankPersistenceService, times(1)).updateBank(bank);
        verify(accountPersistenceService, times(1)).save(savingsAccount);
    }

    @Test
    void testCreateJointCheckingAccount() {
        // Arrange
        Bank bank = TestDataFactory.createBank("My Bank", "MB001");
        User user1 = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        User user2 = TestDataFactory.createUser("John", "Smith", "js@gmail.com");

        // Act
        JointCheckingAccount jointCheckingAccount = accountService.createJointCheckingAccount(bank, user1, user2, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        // Assert
        assertNotNull(jointCheckingAccount);
        assertEquals(BigDecimal.valueOf(1000), jointCheckingAccount.getBalance());
        assertEquals(CurrencyCode.EUR, jointCheckingAccount.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), jointCheckingAccount.getOverdraftLimit());
        assertEquals(user1, jointCheckingAccount.getOwner());
        assertEquals(user2, jointCheckingAccount.getSecondOwner());
        verify(userPersistenceService, times(1)).updateUser(user1);
        verify(userPersistenceService, times(1)).updateUser(user2);
        verify(bankPersistenceService, times(1)).updateBank(bank);
        verify(accountPersistenceService, times(1)).save(jointCheckingAccount);
    }

//    Test Deposit
    @Test
    void testDeposit() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        DepositTransaction depositTransaction = TestDataFactory.createDepositTransaction(BigDecimal.valueOf(500));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
        when(transactionService.createDepositTransaction(BigDecimal.valueOf(500))).thenReturn(depositTransaction);

        // Act
        accountService.deposit(checkingAccount.getAccountId(), BigDecimal.valueOf(500));

        // Assert
        assertEquals(BigDecimal.valueOf(1500), checkingAccount.getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
        verify(transactionService, times(1)).createDepositTransaction(BigDecimal.valueOf(500));
    }

    @Test
    void testDeposit_NegativeAmount() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.deposit(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
        assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
    }

//    Test Withdraw
    @Test
    void testWithdraw() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        WithdrawTransaction withdrawTransaction = TestDataFactory.createWithdrawTransaction(BigDecimal.valueOf(500));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
        when(transactionService.createWithdrawTransaction(BigDecimal.valueOf(500))).thenReturn(withdrawTransaction);

        // Act
        accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(500));

        // Assert
        assertEquals(BigDecimal.valueOf(500), checkingAccount.getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
        verify(transactionService, times(1)).createWithdrawTransaction(BigDecimal.valueOf(500));
    }

    @Test
    void testWithdraw_ExceedsBalanceWithinOverdraftLimit() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(100), CurrencyCode.EUR, BigDecimal.valueOf(500));
        WithdrawTransaction withdrawTransaction = TestDataFactory.createWithdrawTransaction(BigDecimal.valueOf(300));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);
        when(transactionService.createWithdrawTransaction(BigDecimal.valueOf(300))).thenReturn(withdrawTransaction);

        // Act
        accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(300));

        // Assert
        assertEquals(BigDecimal.valueOf(-200), checkingAccount.getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(checkingAccount);
        verify(transactionService, times(1)).createWithdrawTransaction(BigDecimal.valueOf(300));
    }

    @Test
    void testWithdraw_ExceedsOverdraftLimit() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(100), CurrencyCode.EUR, BigDecimal.valueOf(200));
        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act & Assert
        Exception exception = assertThrows(OverdraftLimitExceededException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(400)));
        assertEquals("Withdraw Failed: Overdraft limit exceeded", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(100), CurrencyCode.EUR, 2.0);

        when(accountPersistenceService.getAccountById(savingsAccount.getAccountId())).thenReturn(savingsAccount);

        // Act & Assert
        Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(savingsAccount.getAccountId(), BigDecimal.valueOf(200)));
        assertEquals("Withdraw Filed: Insufficient funds", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(savingsAccount);
    }

    @Test
    void testWithdraw_NegativeAmount() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(checkingAccount.getAccountId(), BigDecimal.valueOf(-500)));
        assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(checkingAccount);
    }

//    Test Transfer
    @Test
    void testTransfer() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 2.0);
        TransferTransaction transferTransaction = TestDataFactory.createTransferTransaction(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
        when(transactionService.createTransferTransaction(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId())).thenReturn(transferTransaction);

        // Act
        accountService.transfer(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        assertEquals(BigDecimal.valueOf(500), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(500), toAccount.getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(1)).updateAccount(toAccount);
        verify(transactionService, times(1)).createTransferTransaction(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());
    }

    @Test
    void testTransfer_DifferentCurrencies() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.USD, 2.0);
        TransferTransaction fromTransaction = TestDataFactory.createTransferTransaction(BigDecimal.valueOf(500), fromAccount.getAccountId(), toAccount.getAccountId());
        TransferTransaction toTransaction = TestDataFactory.createTransferTransaction(BigDecimal.valueOf(530), fromAccount.getAccountId(), toAccount.getAccountId());

        BigDecimal amount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP);

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);
        when(currencyConversionService.convertAmount(amount, CurrencyCode.EUR, CurrencyCode.USD)).thenReturn(convertedAmount);
        when(transactionService.createTransferTransaction(amount, fromAccount.getAccountId(), toAccount.getAccountId())).thenReturn(fromTransaction);
        when(transactionService.createTransferTransaction(convertedAmount, fromAccount.getAccountId(), toAccount.getAccountId())).thenReturn(toTransaction);

        // Act
        accountService.transfer(amount, fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP), toAccount.getBalance());  // Converted amount
        verify(accountPersistenceService, times(1)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(1)).updateAccount(toAccount);
        verify(transactionService, times(1)).createTransferTransaction(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), fromAccount.getAccountId(), toAccount.getAccountId());
        verify(transactionService, times(1)).createTransferTransaction(BigDecimal.valueOf(530).setScale(2, RoundingMode.HALF_UP), fromAccount.getAccountId(), toAccount.getAccountId());
    }

    @Test
    void testTransfer_InsufficientFunds() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(100), CurrencyCode.EUR, 2.0);
        CheckingAccount toAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, BigDecimal.valueOf(100));

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        // Act & Assert
        Exception exception = assertThrows(InsufficientFundsException.class, () -> accountService.transfer(BigDecimal.valueOf(200), fromAccount.getAccountId(), toAccount.getAccountId()));
        assertEquals("Withdraw Filed: Insufficient funds", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(0)).updateAccount(toAccount);
    }

    @Test
    void testTransfer_NegativeAmount() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount fromAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        SavingsAccount toAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 2.0);

        when(accountPersistenceService.getAccountById(fromAccount.getAccountId())).thenReturn(fromAccount);
        when(accountPersistenceService.getAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.transfer(BigDecimal.valueOf(-500), fromAccount.getAccountId(), toAccount.getAccountId()));
        assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
        verify(accountPersistenceService, times(0)).updateAccount(fromAccount);
        verify(accountPersistenceService, times(0)).updateAccount(toAccount);
    }

//    Test Get Account By ID
    @Test
    void testGetAccountById() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act
        Account retrievedAccount = accountService.getAccountById(checkingAccount.getAccountId());

        // Assert
        assertNotNull(retrievedAccount);
        assertEquals(checkingAccount, retrievedAccount);
        verify(accountPersistenceService, times(1)).getAccountById(checkingAccount.getAccountId());
    }

    @Test
    void testGetAccountById_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountById(null));
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById("123"));
    }

//    Test Add Interest
    @Test
    void testAddInterest() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        SavingsAccount savingsAccount = TestDataFactory.createSavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 2.0);

        // Act
        accountService.addInterest(savingsAccount);

        // Assert
        assertEquals(BigDecimal.valueOf(1020.00).setScale(2, RoundingMode.HALF_UP), savingsAccount.getBalance());
        verify(accountPersistenceService, times(1)).updateAccount(savingsAccount);
    }

//    Test Get Transaction History
    @Test
    void testGetTransactionHistory() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));
        DepositTransaction depositTransaction = TestDataFactory.createDepositTransaction(BigDecimal.valueOf(500));
        WithdrawTransaction withdrawTransaction = TestDataFactory.createWithdrawTransaction(BigDecimal.valueOf(300));

        checkingAccount.getTransactionHistory().add(depositTransaction);
        checkingAccount.getTransactionHistory().add(withdrawTransaction);

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act
        String transactionHistory = accountService.getTransactionHistory(checkingAccount.getAccountId());

        // Assert
        assertNotNull(transactionHistory);
        assertTrue(transactionHistory.contains(depositTransaction.toString()));
        assertTrue(transactionHistory.contains(withdrawTransaction.toString()));
        verify(accountPersistenceService, times(1)).getAccountById(checkingAccount.getAccountId());
    }

//    Test Get Balance
    @Test
    void testGetBalance() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act
        BigDecimal balance = accountService.getBalance(checkingAccount.getAccountId());

        // Assert
        assertEquals(BigDecimal.valueOf(1000), balance);
        verify(accountPersistenceService, times(1)).getAccountById(checkingAccount.getAccountId());
    }

//    Test Get Overdraft Limit
    @Test
    void testGetOverdraftLimit() {
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.getAccountById(checkingAccount.getAccountId())).thenReturn(checkingAccount);

        // Act
        BigDecimal overdraftLimit = accountService.getOverdraftLimit(checkingAccount.getAccountId());

        // Assert
        assertEquals(BigDecimal.valueOf(1000), overdraftLimit);
        verify(accountPersistenceService, times(1)).getAccountById(checkingAccount.getAccountId());
    }

//    Test Delete Account
    @Test
    void testDeleteAccount() {
        // Arrange
        User user = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
        CheckingAccount checkingAccount = TestDataFactory.createCheckingAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, BigDecimal.valueOf(1000));

        when(accountPersistenceService.deleteAccount(checkingAccount.getAccountId())).thenReturn(true);

        // Act
        boolean wasDeleted = accountService.deleteAccount(checkingAccount.getAccountId());

        // Assert
        assertTrue(wasDeleted);
        verify(accountPersistenceService, times(1)).deleteAccount(checkingAccount.getAccountId());
    }

    @Test
    void testDeleteAccount_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountService.deleteAccount(null));
    }

    @Test
    void testDeleteAccount_NotFound() {
        // Arrange
        String accountId = "123";
        when(accountPersistenceService.deleteAccount(accountId)).thenReturn(false);

        // Act
        boolean wasDeleted = accountService.deleteAccount(accountId);

        // Assert
        assertFalse(wasDeleted);
        verify(accountPersistenceService, times(1)).deleteAccount(accountId);
    }

}

