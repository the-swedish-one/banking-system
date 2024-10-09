package com.bankingsystem.services;

import com.bankingsystem.models.Bank;
import com.bankingsystem.models.Person;
import com.bankingsystem.models.User;
import com.bankingsystem.persistence.BankPersistenceService;
import com.bankingsystem.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @Mock
    private BankPersistenceService bankPersistenceService;

    @InjectMocks
    private BankService bankService;

    @Test
    void testCreateBank() {
        // Act
        Bank createdBank = bankService.createBank("My Bank", "MB001");

        // Assert
        assertNotNull(createdBank);
        assertEquals("MB001", createdBank.getBic());
        verify(bankPersistenceService, times(1)).createBank(any(Bank.class));
    }

//    @Test
//    void testGetAllBankUsers() {
//        // Arrange
//        String bic = "MB001";
//        Bank bank = TestDataFactory.createBank("My Bank", bic);
//        User user1 = TestDataFactory.createUser("Jane", "Doe", "jd@gmail.com");
//        User user2 = TestDataFactory.createUser("John", "Smith", "js@gmail.com");
//
//        bank.getUsers().add(user1);
//        bank.getUsers().add(user2);
//
//        when(bankPersistenceService.getBankByBic(bic)).thenReturn(bank);
//
//        // Act
//        String result = bankService.getAllBankUsers(bic);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.contains("Users of bank My Bank:"));
//        assertTrue(result.contains("Jane Doe"));
//        assertTrue(result.contains("John Smith"));
//        verify(bankPersistenceService, times(1)).getBankByBic(bic);
//    }


    @Test
    void testGetAllBankAccounts() {
        // Arrange

        // Act

        // Assert
    }

}
