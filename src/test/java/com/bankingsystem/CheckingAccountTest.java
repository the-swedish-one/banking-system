package com.bankingsystem;

import com.bankingsystem.models.*;
import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckingAccountTest {

    private CheckingAccount account;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        Person person = new Person("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        user = new User(person);
    }

    @BeforeEach
    void beforeEach() {
        account = new CheckingAccount(user, 1000, CurrencyCode.EUR, 1000);
    }

    @Test
    void testCheckingAccount() {
        assertEquals(1000, account.getBalance());
        assertEquals(CurrencyCode.EUR, account.getCurrency());
        assertEquals(1000, account.getOverdraftLimit());
    }

    @Test
    void testSetOverdraftLimit() {
        account.setOverdraftLimit(2000);
        assertEquals(2000, account.getOverdraftLimit());
    }

    @Test
    void testDeposit() {
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void testDepositNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-500);
        });
        assertEquals("Deposit failed: Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdraw() {
        account.withdraw(500);
        assertEquals(500, account.getBalance());
    }

    @Test
    void testWithdrawNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(-500);
        });
        assertEquals("Withdraw Failed: Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithdraw_OverdraftLimitExceeded() {
        OverdraftLimitExceededException exception = assertThrows(OverdraftLimitExceededException.class, () -> {
            account.withdraw(5000);
        });

        assertEquals("Withdraw Failed: Overdraft limit exceeded", exception.getMessage());
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testToString() {
        String expected = "CheckingAccount{iban=null'owner=User{person=Jane Doe}overdraftLimit=1000'}";
        assertTrue(account.toString().contains("CheckingAccount"));
        assertTrue(account.toString().contains("overdraftLimit=1000"));
    }
}
