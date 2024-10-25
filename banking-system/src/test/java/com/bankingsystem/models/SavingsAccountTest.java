package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTest {

    private SavingsAccount account;

    @BeforeEach
    void beforeEach() {
        PersonDetails person = new PersonDetails("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        User user = new User(person);
        account = new SavingsAccount(user, BigDecimal.valueOf(1000), CurrencyCode.EUR, 1.5);
    }

    @Test
    void testSavingsAccount() {
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        assertEquals(CurrencyCode.EUR, account.getCurrency());
        assertEquals(1.5, account.getInterestRatePercentage());
    }

    @Test
    void testSetInterestRate() {
        account.setInterestRatePercentage(2.0);
        assertEquals(2.0, account.getInterestRatePercentage());
    }

    @Test
    void testDeposit() {
        account.deposit(new BigDecimal("500"));
        assertEquals(new BigDecimal("1500"), account.getBalance());
    }

    @Test
    void testDepositNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(BigDecimal.valueOf(-500));
        });
        assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdraw() {
        account.withdraw(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    void testWithdrawNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(BigDecimal.valueOf(-500));
        });
        assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(BigDecimal.valueOf(1500));
        });

        assertEquals("Withdraw Filed: Insufficient funds", exception.getMessage());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void testToString() {
        String expected = "SavingsAccount{iban=null'owner=User{person=John Doe}interestRatePercentage=1.5'}";
        assertTrue(account.toString().contains("SavingsAccount"));
        assertTrue(account.toString().contains("interestRatePercentage=1.5"));
    }
}
