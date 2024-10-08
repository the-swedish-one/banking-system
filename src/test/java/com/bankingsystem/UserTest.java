package com.bankingsystem;

import com.bankingsystem.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private static Person person;
    private User user;

    @BeforeAll
    static void beforeAll() {
        person = new Person("Jane", "Doe", "jd@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");

    }

    @BeforeEach
    void beforeEach() {
        user = new User(person);
    }

    @Test
    void testUser() {
        assertEquals("Jane", user.getPerson().getFirstName());
        assertEquals("Doe", user.getPerson().getLastName());
        assertNotNull(user.getUserId());
        assertEquals(0, user.getAccounts().size());
    }

    @Test
    void testSetPerson() {
        Person newPerson = new Person("John", "Doe", "jd2@gmail.com", "Address Line 1", "Address Line 2", "City", "Country");
        user.setPerson(newPerson);
        assertEquals("John", user.getPerson().getFirstName());
    }


    @Test
    void testToString() {
        assertTrue(user.toString().contains("firstName='Jane'"));
        assertTrue(user.toString().contains("lastName='Doe'"));
    }

    @Test
    void testEqualsAndHashCode() {
        User user2 = new User(person);
        user2.setPerson(user.getPerson());

        assertNotEquals(user, user2);
    }
}
