package com.bankingsystem.services;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.TransactionNotFoundException;
import com.bankingsystem.persistence.TransactionPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        transactionService.createDepositTransaction(1000);

        // Assert
        verify(transactionPersistenceService, times(1)).createDepositTransaction(any(DepositTransaction.class));
    }

    @Test
    void testCreateDepositTransaction_NegativeAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createDepositTransaction(-1000));
    }

    @Test
    void testCreateDepositTransaction_ZeroAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createDepositTransaction(0));
    }

//    Test Create Withdraw Transaction
    @Test
    void testCreateWithdrawTransaction() {
        // Act
        transactionService.createWithdrawTransaction(1000);

        // Assert
        verify(transactionPersistenceService, times(1)).createWithdrawTransaction(any(WithdrawTransaction.class));
    }

    @Test
    void testCreateWithdrawTransaction_NegativeAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createWithdrawTransaction(-1000));
    }

    @Test
    void testCreateWithdrawTransaction_ZeroAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createWithdrawTransaction(0));
    }

//    Test Create Transfer Transaction
    @Test
    void testCreateTransferTransaction() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, 1000, CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, 0, CurrencyCode.EUR, 1.5);

        // Act
        transactionService.createTransferTransaction(1000, fromAccount.getAccountId(), toAccount.getAccountId());

        // Assert
        verify(transactionPersistenceService, times(1)).createTransferTransaction(any(TransferTransaction.class));
    }

    @Test
    void testCreateTransferTransaction_NegativeAmount() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, 1000, CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, 0, CurrencyCode.EUR, 1.5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransferTransaction(-1000, fromAccount.getAccountId(), toAccount.getAccountId()));
    }

    @Test
    void testCreateTransferTransaction_ZeroAmount() {
        // Arrange
        User user = TestDataFactory.createUser("John", "Doe", "jd@gmail.com");
        SavingsAccount fromAccount = new SavingsAccount(user, 1000, CurrencyCode.EUR, 1.5);
        SavingsAccount toAccount = new SavingsAccount(user, 0, CurrencyCode.EUR, 1.5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransferTransaction(0, fromAccount.getAccountId(), toAccount.getAccountId()));
    }

//    Test Get Transaction By Id
    @Test
    void testGetTransactionById() {
        // Arrange
        DepositTransaction transaction = TestDataFactory.createDepositTransaction(1000);
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
        DepositTransaction depositTransaction = TestDataFactory.createDepositTransaction(1000);
        WithdrawTransaction withdrawTransaction = TestDataFactory.createWithdrawTransaction(500);
        TransferTransaction transferTransaction = TestDataFactory.createTransferTransaction(1000, "fromAccountId", "toAccountId");

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
        DepositTransaction transaction = TestDataFactory.createDepositTransaction(1000);
        String transactionId = transaction.getTransactionId();

        when(transactionPersistenceService.deleteTransaction(transactionId)).thenReturn(true);

        // Act
        boolean isDeleted = transactionService.deleteTransaction(transactionId);

        // Assert
        assertTrue(isDeleted);
        verify(transactionPersistenceService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void testDeleteTransaction_NonExistent() {
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
