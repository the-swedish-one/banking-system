package com.bankingsystem.service;

import com.bankingsystem.model.*;
import com.bankingsystem.exception.TransactionNotFoundException;
import com.bankingsystem.persistence.TransactionPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionPersistenceService transactionPersistenceService;

    @InjectMocks
    private TransactionService transactionService;

//    Test Create Deposit Transaction
    @Test
    void testCreateDepositTransaction() {
        // Act
       DepositTransaction depositTransaction = transactionService.createDepositTransaction(BigDecimal.valueOf(1000));

        // Assert
        verify(transactionPersistenceService, times(1)).save(depositTransaction);
    }

    @Test
    void testCreateDepositTransaction_NegativeAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createDepositTransaction(BigDecimal.valueOf(-1000)));
    }

    @Test
    void testCreateDepositTransaction_ZeroAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createDepositTransaction(BigDecimal.valueOf(0)));
    }

//    Test Create Withdraw Transaction
    @Test
    void testCreateWithdrawTransaction() {
        // Act
       WithdrawTransaction withdrawTransaction = transactionService.createWithdrawTransaction(BigDecimal.valueOf(1000));

        // Assert
        verify(transactionPersistenceService, times(1)).save(withdrawTransaction);
    }

    @Test
    void testCreateWithdrawTransaction_NegativeAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createWithdrawTransaction(BigDecimal.valueOf(-1000)));
    }

    @Test
    void testCreateWithdrawTransaction_ZeroAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createWithdrawTransaction(BigDecimal.valueOf(0)));
    }

//    Test Create Transfer Transaction
    @Test
    void testCreateTransferTransaction() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 1.5);

        // Act
       TransferTransaction transferTransaction = transactionService.createTransferTransaction(BigDecimal.valueOf(1000), fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        verify(transactionPersistenceService, times(1)).save(transferTransaction);
    }

    @Test
    void testCreateTransferTransaction_NegativeAmount() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 1.5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransferTransaction(BigDecimal.valueOf(-1000), fromAccount.getAccountId(), toAccount.getAccountId()));
    }

    @Test
    void testCreateTransferTransaction_ZeroAmount() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, BigDecimal.valueOf(0), CurrencyCode.EUR, 1.5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransferTransaction(BigDecimal.valueOf(0), fromAccount.getAccountId(), toAccount.getAccountId()));
    }

//    Test Get Transaction By Id
    @Test
    void testGetTransactionById() {
        // Arrange
        DepositTransaction transaction = TestDataFactory.createDepositTransaction(BigDecimal.valueOf(1000));
        String transactionId = transaction.getTransactionId();
        when(transactionPersistenceService.getTransactionById(transactionId)).thenReturn(transaction);

        // Act
        Transaction result = transactionService.getTransactionById(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        verify(transactionPersistenceService, times(1)).getTransactionById(transactionId);
    }

    @Test
    void testGetTransactionById_NullId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(null));
    }

    @Test
    void testGetTransactionById_EmptyId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(""));
    }

    @Test
    void testGetTransactionById_NonExistent() {
        // Arrange
        String invalidTransactionId = "nonexistent-transaction-id";
        when(transactionPersistenceService.getTransactionById(invalidTransactionId)).thenReturn(null);

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(invalidTransactionId));
    }

//    Test Get All Transactions
    @Test
    void testGetAllTransactions() {
        // Arrange
        DepositTransaction depositTransaction = TestDataFactory.createDepositTransaction(BigDecimal.valueOf(1000));
        WithdrawTransaction withdrawTransaction = TestDataFactory.createWithdrawTransaction(BigDecimal.valueOf(500));
        TransferTransaction transferTransaction = TestDataFactory.createTransferTransaction(BigDecimal.valueOf(1000), "fromAccountId", "toAccountId");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(depositTransaction);
        transactions.add(withdrawTransaction);
        transactions.add(transferTransaction);

        when(transactionPersistenceService.getAllTransactions()).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(depositTransaction.getTransactionId(), result.get(0).getTransactionId());
        assertEquals(withdrawTransaction.getTransactionId(), result.get(1).getTransactionId());
        assertEquals(transferTransaction.getTransactionId(), result.get(2).getTransactionId());
    }

    @Test
    void testGetAllTransactions_Empty() {
        // Arrange
        when(transactionPersistenceService.getAllTransactions()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getAllTransactions());
    }

//    Test Delete Transaction
    @Test
    void testDeleteTransaction() {
        // Arrange
        DepositTransaction transaction = TestDataFactory.createDepositTransaction(BigDecimal.valueOf(1000));
        String transactionId = transaction.getTransactionId();

        when(transactionPersistenceService.deleteTransaction(transactionId)).thenReturn(true);

        // Act
        boolean isDeleted = transactionService.deleteTransaction(transactionId);

        // Assert
        assertTrue(isDeleted);
        verify(transactionPersistenceService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        // Arrange
        String nonExistentTransactionId = "non-existent-id";

        // Mock the persistence service to return false for a non-existent transaction
        when(transactionPersistenceService.deleteTransaction(nonExistentTransactionId)).thenReturn(false);

        // Act
        boolean isDeleted = transactionService.deleteTransaction(nonExistentTransactionId);

        // Assert
        assertFalse(isDeleted);
        verify(transactionPersistenceService, times(1)).deleteTransaction(nonExistentTransactionId);
    }

}
