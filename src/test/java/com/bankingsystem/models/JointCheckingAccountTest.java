package com.bankingsystem.models;

import com.bankingsystem.models.exceptions.OverdraftLimitExceededException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JointCheckingAccountTest {

    private JointCheckingAccount account;
    private static User user;
    private static User user2;

    @BeforeAll
    static void beforeAll() {
        Person person = new Person("John", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        user = new User(person);
        Person person2 = new Person("Jane", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        user2 = new User(person2);
    }

    @BeforeEach
    void beforeEach() {
        account = new JointCheckingAccount(user, user2,1000, CurrencyCode.EUR, 1000);
    }

    @Test
    void testJointCheckingAccount() {
        assertEquals(1000, account.getBalance());
        assertEquals(CurrencyCode.EUR, account.getCurrency());
        assertEquals(1000, account.getOverdraftLimit());
        assertEquals(user, account.getOwner());
        assertEquals(user2, account.getSecondOwner());
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
        String expected = "JointCheckingAccount{iban=null'owner=JohnDoe'secondOwner=JaneDoe'}";
        assertTrue(account.toString().contains("JointCheckingAccount"));
        assertTrue(account.toString().contains(account.getOwner().getPerson().getFirstName()));
    }
}
