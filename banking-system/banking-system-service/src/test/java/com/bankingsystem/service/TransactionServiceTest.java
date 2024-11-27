package com.bankingsystem.service;

import com.bankingsystem.exception.TransactionNotFoundException;
import com.bankingsystem.model.Transaction;
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

//    Test Create Transaction
    @Test
    void testCreateTransaction() {
        // Arrange
        Transaction t = TestDataFactory.createTransaction();

        // Act
       Transaction transaction = transactionService.createTransaction(t);

        // Assert
        verify(transactionPersistenceService, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_NullTransaction() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(null));
    }

    @Test
    void testCreateTransaction_NullAccountIds() {
        // Arrange
        Transaction t = TestDataFactory.createTransaction(BigDecimal.valueOf(1000), null, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(null));
    }

    @Test
    void testCreateTransaction_NegativeAmount() {
        // Arrange
        Transaction t = TestDataFactory.createTransaction(BigDecimal.valueOf(-1000), 1, 2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(t));
    }

    @Test
    void testCreateTransaction_ZeroAmount() {
        // Arrange
        Transaction t = TestDataFactory.createTransaction(BigDecimal.valueOf(0), 1, 2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(t));
    }

    @Test
    void testCreateTransaction_NullAmount() {
        // Arrange
        Transaction t = TestDataFactory.createTransaction(null, 1, 2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(t));
    }

//    Test Get Transaction By ID
    @Test
    void testGetTransactionById() {
        // Arrange
        Transaction transaction = TestDataFactory.createTransaction();
        int transactionId = transaction.getTransactionId();
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
    void testGetTransactionById_NonExistent() {
        // Arrange
        int invalidTransactionId = 123;
        when(transactionPersistenceService.getTransactionById(invalidTransactionId)).thenReturn(null);

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(invalidTransactionId));
    }

//    Test Get All Transactions
    @Test
    void testGetAllTransactions() {
        // Arrange
        Transaction depositTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(1000), null, 1);
        Transaction withdrawTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(500), 1, null);
        Transaction transferTransaction = TestDataFactory.createTransaction(BigDecimal.valueOf(1000), 1, 2);

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
        Transaction transaction = TestDataFactory.createTransaction();
        int transactionId = transaction.getTransactionId();

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
        int nonExistentTransactionId = 123;

        // Mock the persistence service to return false for a non-existent transaction
        when(transactionPersistenceService.deleteTransaction(nonExistentTransactionId)).thenReturn(false);

        // Act
        boolean isDeleted = transactionService.deleteTransaction(nonExistentTransactionId);

        // Assert
        assertFalse(isDeleted);
        verify(transactionPersistenceService, times(1)).deleteTransaction(nonExistentTransactionId);
    }

}
